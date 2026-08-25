package com.fzujxl.aicomputerplatform.service;

import com.fzujxl.aicomputerplatform.dto.AuthResponse;
import com.fzujxl.aicomputerplatform.dto.LoginRequest;
import com.fzujxl.aicomputerplatform.dto.RegisterRequest;
import com.fzujxl.aicomputerplatform.entity.User;
import com.fzujxl.aicomputerplatform.exception.BusinessException;
import com.fzujxl.aicomputerplatform.utils.JwtUtil;
import com.fzujxl.aicomputerplatform.utils.PasswordEncoder;
import lombok.extern.slf4j.Slf4j;
import com.fzujxl.aicomputerplatform.mapper.UserMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Slf4j
@Service
public class UserServiceImpl implements UserService {

    //构造器注入，依赖注入UserMapper，是@Autowired的优化写法
    private final UserMapper userMapper;
    private final JwtUtil jwtUtil;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;

    public UserServiceImpl(UserMapper userMapper, JwtUtil jwtUtil,
                           PasswordEncoder passwordEncoder, EmailService emailService) {
        this.userMapper = userMapper;
        this.jwtUtil = jwtUtil;
        this.passwordEncoder = passwordEncoder;
        this.emailService = emailService;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    //注册用户
    public AuthResponse register(RegisterRequest registerRequest){
        log.info("用户注册请求: username={}, email={}", registerRequest.getUsername(),
                registerRequest.getEmail());

        //校验用户名,邮箱是否已存在,如果已存在，抛出异常
        User existNameUser =userMapper.selectBYUserName(registerRequest.getUsername());
        if(existNameUser !=null) {
            log.warn("用户名已存在");
            throw new BusinessException("用户名已存在");
        }

        User existEmailUser = userMapper.selectBYEmail(registerRequest.getEmail());
        if (existEmailUser !=null){
            log.warn("邮箱已存在");
            throw new BusinessException("邮箱已存在");
        }

        User user = new User();
        user.setUserName(registerRequest.getUsername());
        user.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
        user.setEmail(registerRequest.getEmail());
        user.setPoint(new BigDecimal("50"));
        user.setRole("USER");
        user.setStatus(1);
        user.setCreateTime(LocalDateTime.now());
        user.setUpdateTime(LocalDateTime.now());

        int result = userMapper.insert(user);
        if(result<=0){
            log.error("用户注册失败");
            throw new BusinessException("注册失败，请稍后重试");
        }

        String token = jwtUtil.generateToken(user.getId(), user.getUserName());

        log.info("用户注册成功: userId={}, username={}", user.getId(), user.getUserName());

        try {
            emailService.sendWelcomeEmail(user.getEmail(), user.getUserName());
        } catch (Exception e) {
            log.error("发送欢迎邮件失败，但不影响注册流程", e);
        }

        return new AuthResponse(token, user.getId(), user.getUserName(), user.getEmail());
    }

    //登录
    @Override
    public AuthResponse login(LoginRequest loginRequest){
        log.info("用户登录请求:{}",loginRequest.getAccount());
        User user=userMapper.selectByAccount(loginRequest.getAccount());

        if (user==null){
            throw new BusinessException("账号或者密码错误");
        }
        if(!passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())){
            throw new BusinessException("账号或密码错误");
        }
        if (user.getStatus()!=1){
            throw new BusinessException("账号已被禁用");
        }

        String token = jwtUtil.generateToken(user.getId(), user.getUserName());

        log.info("用户登录成功: userId={}, username={}", user.getId(), user.getUserName());

        return new AuthResponse(token, user.getId(), user.getUserName(), user.getEmail());
    }

}

