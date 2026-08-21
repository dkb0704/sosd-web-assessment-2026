package com.fzujxl.aicomputerplatform.service.user;

import com.fzujxl.aicomputerplatform.common.ResultCode;
import com.fzujxl.aicomputerplatform.config.SignConfig;
import com.fzujxl.aicomputerplatform.dto.sign.SignResponse;
import com.fzujxl.aicomputerplatform.dto.user.*;
import com.fzujxl.aicomputerplatform.entity.User;
import com.fzujxl.aicomputerplatform.exception.BusinessException;
import com.fzujxl.aicomputerplatform.service.email.EmailService;
import com.fzujxl.aicomputerplatform.utils.JwtUtil;
import com.fzujxl.aicomputerplatform.utils.PasswordEncoder;
import com.fzujxl.aicomputerplatform.utils.RedisKeys;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import lombok.extern.slf4j.Slf4j;
import com.fzujxl.aicomputerplatform.mapper.user.UserMapper;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.BeanUtils;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import org.springframework.util.StringUtils;

@Slf4j
@Service
public class UserServiceImpl implements UserService {

    //构造器注入，依赖注入UserMapper，是@Autowired的优化写法
    private final UserMapper userMapper;
    private final JwtUtil jwtUtil;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;
    private final StringRedisTemplate stringRedisTemplate;
    private final SignConfig signConfig;
    private final RedissonClient redissonClient;

    public UserServiceImpl(UserMapper userMapper, JwtUtil jwtUtil,
                           PasswordEncoder passwordEncoder, EmailService emailService,
                           StringRedisTemplate stringRedisTemplate, SignConfig signConfig, RedissonClient redissonClient) {
        this.userMapper = userMapper;
        this.jwtUtil = jwtUtil;
        this.passwordEncoder = passwordEncoder;
        this.emailService = emailService;
        this.stringRedisTemplate = stringRedisTemplate;
        this.signConfig = signConfig;
        this.redissonClient = redissonClient;
    }


    //登录
    @Override
    public AuthResponse login(LoginRequest request){
        log.info("用户登录请求:{}",request.getAccount());
        User existUser=userMapper.selectByAccount(request.getAccount());

        if (existUser==null){
            log.info("用户注册请求: username={}", request.getAccount());
            String lockKey = "register"+request.getAccount();
            RLock lock = redissonClient.getLock(lockKey);
            boolean isLock = false;
            try {
                isLock = lock.tryLock(1,10, TimeUnit.SECONDS);
                if(!isLock){
                    throw new BusinessException(ResultCode.INTERNAL_ERROR, "操作太频繁,请稍后重试");
                }
                //校验用户名,邮箱是否已存在,如果已存在，抛出异常
                User repeatedUser =userMapper.selectByUserName(request.getAccount());
                if(repeatedUser !=null) {
                    log.warn("用户名或邮箱已存在");
                    throw new BusinessException("用户名或邮箱已存在");
                }

                User user = new User();
                if (request.getAccount().matches("^\\S*(?=\\S{8,})(?=\\S*\\d)(?=\\S*[A-Z])"
                        + "(?=\\S*[a-z])(?=\\S*[!@#$%^&*?])\\S*$")){
                    user.setUserName(null);
                    user.setEmail(request.getAccount());
                }

                user.setPassword(passwordEncoder.encode(request.getPassword()));
                user.setUserName(request.getAccount());
                user.setPoint(new BigDecimal("50"));
                user.setRole("USER");
                user.setStatus(1);
                user.setCreateTime(LocalDateTime.now());
                user.setUpdateTime(LocalDateTime.now());
                user.setSignDays(0);
                user.setLastSignDate(null);
                user.setDeleted(0);
                int result = userMapper.insert(user);

                if(result<=0){
                    log.error("用户注册失败");
                    throw new BusinessException(ResultCode.INTERNAL_ERROR, "注册失败，请稍后重试");
                }
                //根据用户名或者邮箱查询用户token_version
                User tokenVersionUser = userMapper.selectVersion(request.getAccount());
                Long tokenVersion = tokenVersionUser.getTokenVersion();
                String token = jwtUtil.generateToken(user.getId(), request.getAccount(), tokenVersion);

                log.info("用户注册成功: userId={}, username={}", user.getId(), request.getAccount());
                // 在返回之前，异步发送邮件
                CompletableFuture.runAsync(() -> {
                    try {
                        emailService.sendWelcomeEmail(user.getEmail(), request.getAccount());
                    } catch (Exception e) {
                        log.error("发送欢迎邮件失败，但不影响注册流程", e);
                    }
                });
                return new AuthResponse(token, user.getId(), user.getUserName(), user.getEmail());
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                log.error("锁等待被中断：{}", request.getAccount(),e);
                throw new BusinessException(ResultCode.INTERNAL_ERROR, "锁等待被中断");
            } finally{
                if(isLock &&lock.isHeldByCurrentThread()){
                    lock.unlock();
                }
            }
        }
        if(!passwordEncoder.matches(request.getPassword(), existUser.getPassword())){
            throw new BusinessException("密码错误");
        }
        if (existUser.getStatus()!=1){
            throw new BusinessException("账号已被禁用");
        }
        User tokenVersionUser = userMapper.selectVersion(request.getAccount());
        Long tokenVersion = tokenVersionUser.getTokenVersion();
        String token = jwtUtil.generateToken(existUser.getId(), existUser.getUserName(), tokenVersion);

        log.info("用户登录成功: userId={}, username={}", existUser.getId(), existUser.getUserName());

        return new AuthResponse(token, existUser.getId(), existUser.getUserName(), existUser.getEmail());
    }

    @Override
    public UserProfileResponse getCurrentUserInfo(Long userId) {
        User user = validateUserAndStatus(userId);

        UserProfileResponse response = new UserProfileResponse();
        BeanUtils.copyProperties(user, response);
        return response;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public UserProfileResponse updateProfile(Long userId, UpdateProfileRequest request) {
        User user = validateUserAndStatus(userId);

        // 检查传入值与数据库现有值是否完全相同
        boolean hasChange = (StringUtils.hasText(request.getUserName()) && !request.getUserName().equals(user.getUserName())) ||
                (StringUtils.hasText(request.getEmail()) && !request.getEmail().equals(user.getEmail()));

        if (!hasChange) {
            UserProfileResponse response = new UserProfileResponse();
            BeanUtils.copyProperties(user, response);
            response.setUpdated(false);
            return response;
        }

        // 检查唯一性
        if (StringUtils.hasText(request.getUserName())) {
            User existingUser = userMapper.selectByUserNameExcludeUserId(request.getUserName(), userId);
            if (existingUser != null) {
                throw new BusinessException(ResultCode.BAD_REQUEST, "用户名已被占用");
            }
        }
        if (StringUtils.hasText(request.getEmail())) {
            User existingUser = userMapper.selectByEmailExcludeUserId(request.getEmail(), userId);
            if (existingUser != null) {
                throw new BusinessException(ResultCode.BAD_REQUEST, "邮箱已被占用");
            }
        }

        try {
            User updateUser = new User();
            updateUser.setId(userId);
            updateUser.setUserName(request.getUserName());
            updateUser.setEmail(request.getEmail());

            int result = userMapper.updateUser(updateUser);
            if (result <= 0) {
                throw new BusinessException(ResultCode.INTERNAL_ERROR, "更新失败");
            }

            // 重新查询更新后的用户信息
            User updatedUser = userMapper.selectById(userId);
            UserProfileResponse response = new UserProfileResponse();
            BeanUtils.copyProperties(updatedUser, response);
            return response;
        } catch (DuplicateKeyException e) {
            log.error("更新用户信息时发生唯一约束冲突: {}", e.getMessage());
            throw new BusinessException(ResultCode.BAD_REQUEST, "用户名或邮箱已被占用");
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void changePassword(Long userId, ChangePasswordRequest request, String token) {
        User user = validateUserAndStatus(userId);

        // 验证旧密码
        if (!passwordEncoder.matches(request.getOldPassword(), user.getPassword())) {
            throw new BusinessException(ResultCode.BAD_REQUEST, "原密码错误");
        }

        // 检查新旧密码是否相同
        if (request.getOldPassword().equals(request.getNewPassword())) {
            throw new BusinessException(ResultCode.BAD_REQUEST, "新密码不能与旧密码相同");
        }

        // 加密新密码并更新
        String encodedNewPassword = passwordEncoder.encode(request.getNewPassword());
        user.setPassword(encodedNewPassword);

        int result = userMapper.updateUser(user);
        if (result <= 0) {
            throw new BusinessException(ResultCode.INTERNAL_ERROR, "密码更新失败");
        }
        try {
            stringRedisTemplate.opsForValue().set(String.format(RedisKeys.TOKEN_VERSION_KEY, userId), String.valueOf(user.getTokenVersion()));
        } catch (Exception e) {
            throw new BusinessException(ResultCode.INTERNAL_ERROR, "更新缓存token版本失败");
        }

    }

    @Override
    public SignResponse sign(Long userId) {
        User user = validateUserAndStatus(userId);
        String signDayKey = String.format(RedisKeys.SIGN_DAY_KEY, userId,
                LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd")));

        LocalDate today = LocalDate.now();
        LocalDateTime signTime = LocalDateTime.now();

        checkTodaySigned(signDayKey, today, signTime);

        try {
            int streakDays = calculateStreakDays(user, userId);
            int earnedPoints = calculateEarnedPoints(streakDays);

            // 更新数据库和缓存
            BigDecimal newTotalPoints = user.getPoint().add(new BigDecimal(earnedPoints));
            updateDatabaseAfterSign(userId, newTotalPoints, streakDays);

            // 缓存更新失败不影响签到结果，单独 try-catch
            try {
                updateStreakCache(userId, streakDays);
            } catch (Exception e) {
                log.error("签到缓存更新失败，不影响签到结果，userId={}", userId, e);
            }

            return getSignResponse(earnedPoints, streakDays, newTotalPoints, today);

        } catch (Exception e) {
            // 补偿回滚
            rollbackSignKey(signDayKey);
            log.error("[签到异常] userId={}", userId, e);
            throw new BusinessException(ResultCode.INTERNAL_ERROR, "签到失败，请重试");
        }
    }

    @Override
    public void logout(String token) {
        // 此方法在拦截器中已排除，所以可以安全处理各种情况
        if (!StringUtils.hasText(token)) {
            return;
        }
        addToBlacklist(token);
    }

    @NonNull
    private SignResponse getSignResponse(int earnedPoints, int streakDays, BigDecimal newTotalPoints, LocalDate today) {
        SignResponse response = new SignResponse();
        response.setEarnedPoints(earnedPoints);
        response.setStreakDays(streakDays);
        response.setTotalPoints(newTotalPoints);
        response.setLastSignDate(today);
        return response;
    }

    private User validateUserAndStatus(Long userId) {
        User user = userMapper.selectById(userId);
        if (user == null) {
            throw new BusinessException(ResultCode.NOT_FOUND, "用户不存在");
        }
        if (user.getStatus() != 1) {
            throw new BusinessException(ResultCode.FORBIDDEN, "账号已禁用");
        }
        return user;
    }

    private void checkTodaySigned(String signDayKey,LocalDate today, LocalDateTime signTime) {
        // 计算到第二天零点的秒数
        LocalDateTime nextDayStart = today.plusDays(1).atStartOfDay();
        long ttlSeconds = Duration.between(signTime, nextDayStart).getSeconds();
        long ttlWithBuffer = ttlSeconds + 60;

        // 如果 ttlSeconds <= 0（跨天瞬间）
        if (ttlWithBuffer <= 0) {
            throw new BusinessException(ResultCode.INTERNAL_ERROR, "系统繁忙，请稍后重试");
        }

        Boolean setResult = stringRedisTemplate.opsForValue()
                .setIfAbsent(signDayKey, "1", ttlWithBuffer, TimeUnit.SECONDS);
        if (setResult!=null&&!setResult) {
            throw new BusinessException(ResultCode.BAD_REQUEST, "今日已签到");
        }
    }

    private int calculateStreakDays(User user, Long userId) {
        String streakKey = String.format(RedisKeys.SIGN_STREAK_KEY, userId);
        String lastDateStr = (String) stringRedisTemplate.opsForHash().get(streakKey, "lastDate");
        String streakDaysStr = (String) stringRedisTemplate.opsForHash().get(streakKey, "streakDays");

        LocalDate lastSignDate;
        int streakDays;

        if (lastDateStr == null || streakDaysStr == null) {
            lastSignDate = user.getLastSignDate() != null ? user.getLastSignDate().toLocalDate() : null;
            streakDays = user.getSignDays() != null ? user.getSignDays() : 0;
        } else {
            lastSignDate = LocalDate.parse(lastDateStr);
            streakDays = Integer.parseInt(streakDaysStr);
        }

        LocalDate today = LocalDate.now();
        if (lastSignDate == null || !lastSignDate.equals(today.minusDays(1))) {
            return 1; // 不是连续签到，重置为1
        } else {
            return streakDays + 1; // 连续签到
        }
    }

    private int calculateEarnedPoints(int streakDays) {
        int earnedPoints = signConfig.getBasePoints();
        if (streakDays > 0 && streakDays % signConfig.getStreakCycle() == 0) {
            earnedPoints += signConfig.getStreakBonus();
        }
        return earnedPoints;
    }

    private void updateStreakCache(Long userId, int streakDays) {
        String streakKey = String.format(RedisKeys.SIGN_STREAK_KEY, userId);
        stringRedisTemplate.opsForHash().put(streakKey, "lastDate", LocalDate.now().toString());
        stringRedisTemplate.opsForHash().put(streakKey, "streakDays", String.valueOf(streakDays));
        stringRedisTemplate.expire(streakKey, 30, TimeUnit.DAYS);
    }

    private void updateDatabaseAfterSign(Long userId, BigDecimal newTotalPoints, int streakDays) {
        int updateResult = userMapper.updateSignInfo(userId, newTotalPoints, LocalDate.now(), streakDays);
        if (updateResult <= 0) {
            throw new BusinessException(ResultCode.INTERNAL_ERROR, "签到失败，请重试");
        }
    }

    private void rollbackSignKey(String signDayKey) {
        try {
            stringRedisTemplate.delete(signDayKey);
        } catch (Exception e) {
            log.error("[安全告警] 签到异常补偿失败，Redis防刷Key删除失败，可能导致用户无法再次签到! key={}", signDayKey, e);
        }
    }

    private void addToBlacklist(String token) {
        if (!StringUtils.hasText(token)) {
            return;
        }

        try {
            // 1. 调用新的公共方法，一次性解析，获取所有需要的信息
            Claims claims = jwtUtil.getAllClaimsFromToken(token);
            Instant expiration = claims.getExpiration().toInstant();
            Instant now = Instant.now();

            // 2. 只有当 token 尚未过期时，才加入黑名单
            if (expiration.isAfter(now)) {
                // 3. 从 Claims 中直接提取 jti，避免再次解析
                String jti = claims.getId();
                String blacklistKey;

                if (StringUtils.hasText(jti)) {
                    blacklistKey = RedisKeys.JWT_BLACKLIST_PREFIX + jti;
                } else {
                    // 旧 Token 兼容：使用 SHA-256 指纹
                    blacklistKey = RedisKeys.JWT_BLACKLIST_PREFIX + jwtUtil.generateFingerprint(token);
                }

                Duration remainingDuration = Duration.between(now, expiration);
                stringRedisTemplate.opsForValue().set(blacklistKey, "1", remainingDuration);
            }
        } catch (ExpiredJwtException e) {
            // Token 已过期，无需加入黑名单，这是正常情况，记录 debug 日志
            log.debug("尝试将已过期的 token 加入黑名单，操作已忽略。");
        } catch (Exception e) {
            // 其他解析异常（如格式错误、签名无效等），记录警告
            log.warn("[安全告警] 将 token 加入黑名单时发生解析异常", e);
        }
    }
}