package com.fzujxl.aicomputerplatform.mapper.order;

import com.fzujxl.aicomputerplatform.entity.Order;
import com.fzujxl.aicomputerplatform.entity.RechargePackage;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface OrderMapper {

    @Select("select id,order_no,user_id, package_id, package_name, package_type, points, daily_points," +
            " duration_days, amount, status, trade_no, payment_time, created_time, updated_time from 'order'" +
            "where deleted = 0 order by 'order'.created_time desc,id")
    List<Order> selectAllOrdersInfo();

    @Select("select order_no, package_name ,amount from 'order'" +
            "where deleted = 0 and order_no = #{orderNo}")
    Order selectByOrderNo(String orderNo);

    @Update("update 'order' set status=#{order.status},payment_time=#{order.paymentTime}," +
            "points=(select points from recharge_package where recharge_package.package_name = #{order.packageName})," +
            "daily_points=(select daily_points from recharge_package where recharge_package.package_name = #{order.packageName})," +
            "trade_no=#{order.tradeNo} where order_no=#{order.orderNo}")
    int updateByOrderNo(Order order);

    @Select("select package_name,package_type,points,daily_points,duration_days,current_price from" +
            " recharge_package where id=#{packageId} and status=1 and deleted=0")
    RechargePackage selectByPackageId(Long packageId);

    @Insert("insert into 'order' (order_no,user_id, package_id, package_name, package_type, points," +
            " daily_points,duration_days, amount, status,  created_time) " +
            "values (#{order.orderNo},#{userId},#{order.packageId},#{order.packageName},#{order.packageType}," +
            "#{order.points},#{order.dailyPoints},#{order.duration},#{order.amount},#{order.status},now())")
    int insertOrder(@Param("order") Order order, @Param("userId") Long userId);
}
