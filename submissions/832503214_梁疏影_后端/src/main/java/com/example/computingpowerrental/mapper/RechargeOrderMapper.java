package com.example.computingpowerrental.mapper;

import com.example.computingpowerrental.entity.RechargeOrder;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author Lark
 * @ date 2026/8/15  12:16
 * @ description
 */
@Mapper
public interface RechargeOrderMapper {
    //创建充值订单
    int insert(RechargeOrder order);

    //根据ID查询订单
    RechargeOrder findById(@Param("id") Long id);

    //根据订单号查询订单
    RechargeOrder findByOrderNo(@Param("orderNo") String orderNo);

    //查询用户订单
    List<RechargeOrder> findByUserId(@Param("userId") Long userId);

    //分页查询用户充值订单
    List<RechargeOrder> findPageByUserId(
            @Param("userId") Long userId,
            @Param("status") Integer status,
            @Param("offset") Integer offset,
            @Param("size") Integer size
    );

    //查询用户充值订单总数
    Long countByUserId(
            @Param("userId") Long userId,
            @Param("status") Integer status
    );

    //查询全部订单
    List<RechargeOrder> findAll();

    //管理端分页查询全站充值订单
    List<RechargeOrder> findPage(
            @Param("status") Integer status,
            @Param("offset") Integer offset,
            @Param("size") Integer size
    );

    //管理端查询全站充值订单总数
    Long countAll(
            @Param("status") Integer status
    );

    //支付成功
    int updatePaid(
            @Param("orderNo") String orderNo,
            @Param("pendingStatus") Integer pendingStatus,
            @Param("closedStatus") Integer closedStatus,
            @Param("paidStatus") Integer paidStatus,
            @Param("payTradeNo") String payTradeNo,
            @Param("paidAt") LocalDateTime paidAt
    );

    //关闭订单
    int updateStatus(@Param("id") Long id, @Param("oldStatus") Integer oldStatus, @Param("newStatus") Integer newStatus);

    //查询超时未支付订单
    List<RechargeOrder> findTimeoutOrders(@Param("status") Integer status, @Param("expireTime") LocalDateTime expireTime);

    //管理员手动将异常订单补为已支付
    int adminUpdatePaid(@Param("id") Long id, @Param("pendingStatus") Integer pendingStatus, @Param("closedStatus") Integer closedStatus, @Param("paidStatus") Integer paidStatus, @Param("paidAt") LocalDateTime paidAt);
}
