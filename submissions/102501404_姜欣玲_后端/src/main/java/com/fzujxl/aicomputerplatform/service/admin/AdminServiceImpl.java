package com.fzujxl.aicomputerplatform.service.admin;

import com.fzujxl.aicomputerplatform.common.ResultCode;
import com.fzujxl.aicomputerplatform.dto.PageResultResponse;
import com.fzujxl.aicomputerplatform.dto.admin.*;
import com.fzujxl.aicomputerplatform.dto.admin.order.OrderOperateRequest;
import com.fzujxl.aicomputerplatform.dto.admin.order.OrderOperateResponse;
import com.fzujxl.aicomputerplatform.dto.user.UserProfileResponse;
import com.fzujxl.aicomputerplatform.entity.Order;
import com.fzujxl.aicomputerplatform.entity.RechargePackage;
import com.fzujxl.aicomputerplatform.entity.User;
import com.fzujxl.aicomputerplatform.exception.BusinessException;
import com.fzujxl.aicomputerplatform.mapper.admin.AdminMapper;
import com.fzujxl.aicomputerplatform.utils.SnowFlake;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.List;


@Slf4j
@Service
public class AdminServiceImpl implements AdminService {

    private final AdminMapper adminMapper;
    public AdminServiceImpl(AdminMapper adminMapper) {
        this.adminMapper = adminMapper;
    }

    @Override
    public PageResultResponse<User> queryUsersInfo(int pageNum, int pageSize) {

            PageHelper.startPage(pageNum, pageSize);
            List<User> users = adminMapper.selectAllUsersInfo();
            PageInfo<User> pageInfo = new PageInfo<>(users);
            return PageResultResponse.of(pageInfo);

    }

    @Override
    public UserProfileResponse queryUserInfo(UserInfoQueryRequest request) {
        User user = adminMapper.selectByUniqueKey(request);
        if (user == null) {
            throw new BusinessException(ResultCode.NOT_FOUND, "用户不存在");
        }
        UserProfileResponse response = new UserProfileResponse();
        BeanUtils.copyProperties(user, response);
        return response;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public UserProfileResponse updateUserInfo(AdminUpdateRequest request){
        User user = adminMapper.selectById(request.getId());
        if (user == null) {
            throw new BusinessException(ResultCode.NOT_FOUND, "用户不存在");
        }
        if(request.getRole().equalsIgnoreCase("admin")){
            throw new BusinessException(ResultCode.FORBIDDEN, "没有更新管理员的权限");
        }
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
            User existingUser = adminMapper.selectByUserNameExcludeUserId(request.getUserName(), request.getId());
            if (existingUser != null) {
                throw new BusinessException(ResultCode.BAD_REQUEST, "用户名已被占用");
            }
        }
        if (StringUtils.hasText(request.getEmail())) {
            User existingUser = adminMapper.selectByEmailExcludeUserId(request.getEmail(), request.getId());
            if (existingUser != null) {
                throw new BusinessException(ResultCode.BAD_REQUEST, "邮箱已被占用");
            }
        }

        try {
            User updateUser = new User();
            updateUser.setId(request.getId());
            updateUser.setUserName(request.getUserName());
            updateUser.setEmail(request.getEmail());

            int result = adminMapper.updateUser(updateUser);
            if (result <= 0) {
                throw new BusinessException(ResultCode.INTERNAL_ERROR, "更新失败");
            }

            // 重新查询更新后的用户信息
            User updatedUser = adminMapper.selectById(request.getId());
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
    public UserStatusOperationResponse operateUserStatus(UserStatusOperateRequest request){
        User user = adminMapper.selectByStatusUniqueKey(request);
        if (user == null) {
            throw new BusinessException(ResultCode.NOT_FOUND, "用户不存在");
        }
        int result = adminMapper.statusOperate(request);
        if (result <= 0) {
            throw new BusinessException(ResultCode.INTERNAL_ERROR, "操作失败");
        }
        UserStatusOperationResponse response = new UserStatusOperationResponse();
        BeanUtils.copyProperties(user, response);
        return response;
    }

    @Override
    public PointOperateResponse operateUserPoint(PointOperateRequest request){
        User user = adminMapper.selectByPointUniqueKey(request);
        if (user == null) {
            throw new BusinessException(ResultCode.NOT_FOUND, "用户不存在");
        }
        int result = adminMapper.pointOperate(request);
        if (result <= 0) {
            throw new BusinessException(ResultCode.INTERNAL_ERROR, "操作失败");
        }
        PointOperateResponse response = new PointOperateResponse();
        BeanUtils.copyProperties(user, response);
        return response;
    }

    @Override
    public OrderOperateResponse operateOrder(OrderOperateRequest request){
        RechargePackage rechargePackage = adminMapper.selectByPackageId(request.getPackageId());
        if (rechargePackage == null) {
            throw new BusinessException(ResultCode.NOT_FOUND, "充值套餐不存在");
        }

        String orderNo = SnowFlake.nextIdStr();
        Order order = new Order();
        order.setOrderNo(orderNo);
        BeanUtils.copyProperties(request, order);
        order.setPackageName(rechargePackage.getPackageName());
        order.setPackageType(rechargePackage.getPackageType());
        order.setPoints(rechargePackage.getPoints());
        order.setDailyPoints(rechargePackage.getDailyPoints());
        order.setDuration(rechargePackage.getDuration());
        order.setAmount(rechargePackage.getCurrentPrice());
        order.setCreatedTime(LocalDateTime.now());
        order.setPaymentTime(LocalDateTime.now());

        int result = adminMapper.insertOrder(order);
        if (result <= 0){
            throw new BusinessException(ResultCode.INTERNAL_ERROR, "操作失败");
        }

        return OrderOperateResponse.of(order);
    }

}
