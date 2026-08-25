package com.fzujxl.aicomputerplatform.mapper.admin;

import com.fzujxl.aicomputerplatform.dto.admin.PointOperateRequest;
import com.fzujxl.aicomputerplatform.dto.admin.UserInfoQueryRequest;
import com.fzujxl.aicomputerplatform.dto.admin.UserStatusOperateRequest;
import com.fzujxl.aicomputerplatform.entity.Order;
import com.fzujxl.aicomputerplatform.entity.RechargePackage;
import com.fzujxl.aicomputerplatform.entity.User;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface AdminMapper {

    @Select("select id,user_name,status,point,sign_days,email,role,last_sign_date," +
            "create_time,update_time from 'user' where deleted = 0")
    List<User> selectAllUsersInfo();

    User selectByUniqueKey(@Param("uniqueKey") UserInfoQueryRequest uniqueKey);

    //根据用户名查询用户（查重，排除指定ID）
    @Select("SELECT id FROM 'user' WHERE deleted = 0 AND user_name = #{userName} AND id != #{userId} LIMIT 1")
    User selectByUserNameExcludeUserId(@Param("userName") String userName, @Param("userId") Long userId);

    //根据邮箱查询用户（查重，排除指定ID）
    @Select("SELECT id FROM 'user' WHERE deleted = 0 AND email = #{email} AND id != #{userId} LIMIT 1")
    User selectByEmailExcludeUserId(@Param("email") String email, @Param("userId") Long userId);

    User selectByStatusUniqueKey(@Param("uniqueKey") UserStatusOperateRequest uniqueKey);

    //根据ID查询用户
    @Select("SELECT id FROM 'user' WHERE id = #{id} AND deleted = 0 LIMIT 1")
    User selectById(@Param("id") Long id);

    User selectByPointUniqueKey(@Param("uniqueKey") PointOperateRequest uniqueKey);

    int statusOperate(@Param("request") UserStatusOperateRequest request);

    int pointOperate(@Param("request") PointOperateRequest request);

    int updateUser(User user);

    @Select("select package_name,package_type,points,daily_points,duration_days,current_price from " +
            "recharge_package where id = #{packageId} and deleted = 0")
    RechargePackage selectByPackageId(@Param("packageId") Long packageId);

    @Insert("insert into 'order' (order_no,user_id,package_id,package_name,package_type,points,daily_points,duration_days,amount,status,trade_no,created_time,payment_time) " +
            "values (#{orderNo},#{userId},#{packageId},#{packageName},#{packageType},#{points},#{dailyPoints},#{durationDays},#{amount},#{status},#{tradeNo},#{createdTime},#{paymentTime}) ")
    int insertOrder( Order order);
}
