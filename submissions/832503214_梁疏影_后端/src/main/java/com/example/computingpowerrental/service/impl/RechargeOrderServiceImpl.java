package com.example.computingpowerrental.service.impl;

import com.example.computingpowerrental.entity.ComputePackage;
import com.example.computingpowerrental.entity.RechargeOrder;
import com.example.computingpowerrental.enums.ComputePackageStatus;
import com.example.computingpowerrental.enums.RechargeOrderStatus;
import com.example.computingpowerrental.mapper.RechargeOrderMapper;
import com.example.computingpowerrental.service.ComputePackageService;
import com.example.computingpowerrental.service.RechargeOrderService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.example.computingpowerrental.service.ComputePointService;
import com.example.computingpowerrental.vo.RechargeOrderPageVO;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.UUID;

/**
 * @author Lark
 * @ date 2026/8/16  15:15
 * @ description 充值订单业务实现类
 */
@Service
public class RechargeOrderServiceImpl implements RechargeOrderService{
    //订单有效期30分钟
    private static final int ORDER_EXPIRE_MINUTES = 30;

    private final RechargeOrderMapper rechargeOrderMapper;

    private final ComputePackageService computePackageService;

    private final ComputePointService computePointService;

    public RechargeOrderServiceImpl(RechargeOrderMapper rechargeOrderMapper, ComputePackageService computePackageService,ComputePointService computePointService) {
        this.rechargeOrderMapper = rechargeOrderMapper;
        this.computePackageService = computePackageService;
        this.computePointService = computePointService;
    }

    //创建充值订单
    @Override
    @Transactional
    public RechargeOrder createOrder(Long userId, Long packageId) {

        if (userId == null) {
            throw new RuntimeException("当前用户未登录");
        }

        //查询充值套餐
        ComputePackage computePackage = computePackageService.getById(packageId);

        //只能购买已经上架的套餐
        if (!ComputePackageStatus.ON_SHELF.getCode().equals(computePackage.getStatus())) {
            throw new RuntimeException("当前充值套餐不可购买");
        }

        LocalDateTime now = LocalDateTime.now();

        //创建订单
        RechargeOrder order = new RechargeOrder();

        order.setOrderNo(generateOrderNo());

        order.setUserId(userId);

        order.setPackageId(computePackage.getId());

        //保存下单时套餐名称快照
        order.setPackageName(computePackage.getPackageName());

        //保存下单时算力点数快照
        order.setPoints(computePackage.getPoints());

        //保存下单时价格快照
        order.setAmount(computePackage.getPrice());

        //新订单默认为待支付
        order.setStatus(RechargeOrderStatus.PENDING.getCode());

        //支付交易号初始为空
        order.setPayTradeNo(null);

        //支付时间初始为空
        order.setPaidAt(null);

        //30分钟后过期
        order.setExpiredAt(now.plusMinutes(ORDER_EXPIRE_MINUTES));

        int rows = rechargeOrderMapper.insert(order);

        if (rows == 0) {
            throw new RuntimeException("创建充值订单失败");
        }

        return order;
    }

    //查询当前用户订单详情
    @Override
    public RechargeOrder getUserOrder(Long userId, Long orderId) {

        if (userId == null) {
            throw new RuntimeException("当前用户未登录");
        }

        RechargeOrder order = rechargeOrderMapper.findById(orderId);

        if (order == null) {
            throw new RuntimeException("充值订单不存在");
        }

        //防止查看其他用户的订单
        if (!userId.equals(order.getUserId())) {
            throw new RuntimeException("无权查看该充值订单");
        }

        return order;
    }

    //分页查询当前用户充值订单
    @Override
    public RechargeOrderPageVO pageUserOrders(
            Long userId,
            Integer page,
            Integer size,
            Integer status
    ) {

        if (userId == null) {
            throw new RuntimeException("当前用户未登录");
        }

        if (page == null || page < 1) {
            page = 1;
        }

        if (size == null || size < 1) {
            size = 10;
        }

        //避免一次查询过多数据
        if (size > 100) {
            size = 100;
        }

        int offset = (page - 1) * size;

        List<RechargeOrder> records =
                rechargeOrderMapper.findPageByUserId(
                        userId,
                        status,
                        offset,
                        size
                );

        Long total =
                rechargeOrderMapper.countByUserId(
                        userId,
                        status
                );

        RechargeOrderPageVO vo =
                new RechargeOrderPageVO();

        vo.setRecords(records);
        vo.setPage(page);
        vo.setSize(size);
        vo.setTotal(total);

        return vo;
    }

    //根据订单号查询订单
    @Override
    public RechargeOrder getByOrderNo(String orderNo) {

        RechargeOrder order = rechargeOrderMapper.findByOrderNo(orderNo);

        if (order == null) {throw new RuntimeException("充值订单不存在");}
        return order;
    }

    //支付成功，更新订单并增加用户算力
    @Override
    @Transactional
    public void completePayment(String orderNo, String tradeNo) {
        RechargeOrder order = getByOrderNo(orderNo);

        //已经支付过，直接结束
        //防止支付宝重复通知导致重复充值
        if (RechargeOrderStatus.PAID.getCode().equals(order.getStatus())) {
            return;
        }

        if (!RechargeOrderStatus.PENDING.getCode().equals(order.getStatus())
                && !RechargeOrderStatus.CLOSED.getCode().equals(order.getStatus())) {
            throw new RuntimeException("当前订单状态不可完成支付");
        }

        int rows = rechargeOrderMapper.updatePaid(
                orderNo,
                RechargeOrderStatus.PENDING.getCode(),
                RechargeOrderStatus.CLOSED.getCode(),
                RechargeOrderStatus.PAID.getCode(),
                tradeNo,
                LocalDateTime.now()
        );

        if (rows == 0) {

            RechargeOrder latestOrder = getByOrderNo(orderNo);

            //其他请求已经成功处理过
            if (RechargeOrderStatus.PAID.getCode().equals(latestOrder.getStatus())) {
                return;
            }

            throw new RuntimeException("更新订单支付状态失败");
        }

        //订单成功改为PAID以后增加算力
        computePointService.addPoints(order.getUserId(), order.getPoints());
    }

    //关闭超时未支付订单
    @Override
    @Transactional
    public int closeTimeoutOrders() {

        LocalDateTime now = LocalDateTime.now();

        //查询已经超过支付有效期的待支付订单
        List<RechargeOrder> timeoutOrders =
                rechargeOrderMapper.findTimeoutOrders(
                        RechargeOrderStatus.PENDING.getCode(),
                        now
                );

        if (timeoutOrders == null || timeoutOrders.isEmpty()) {
            return 0;
        }

        int closedCount = 0;

        for (RechargeOrder order : timeoutOrders) {

            int rows =
                    rechargeOrderMapper.updateStatus(
                            order.getId(),
                            RechargeOrderStatus.PENDING.getCode(),
                            RechargeOrderStatus.CLOSED.getCode()
                    );

            if (rows > 0) {
                closedCount++;
            }
        }

        return closedCount;
    }

    //用户主动取消订单
    @Override
    @Transactional
    public void cancelOrder(Long userId, Long orderId) {

        //复用已有方法，同时完成登录、订单存在和订单归属校验
        RechargeOrder order = getUserOrder(userId, orderId);

        //只有待支付订单可以取消
        if (!RechargeOrderStatus.PENDING.getCode().equals(order.getStatus())) {
            throw new RuntimeException("当前订单状态不可取消");
        }

        //只允许 PENDING -> CLOSED
        int rows = rechargeOrderMapper.updateStatus(
                orderId,
                RechargeOrderStatus.PENDING.getCode(),
                RechargeOrderStatus.CLOSED.getCode()
        );

        if (rows == 0) {
            throw new RuntimeException("取消订单失败，订单状态可能已经发生变化");
        }
    }

    //管理端分页查询全站充值订单
    @Override
    public RechargeOrderPageVO adminPageOrders(Integer page, Integer size, Integer status) {

        if (page == null || page < 1) {
            page = 1;
        }

        if (size == null || size < 1) {
            size = 10;
        }

        if (size > 100) {
            size = 100;
        }

        int offset = (page - 1) * size;

        List<RechargeOrder> records = rechargeOrderMapper.findPage(status, offset, size);

        Long total = rechargeOrderMapper.countAll(status);

        RechargeOrderPageVO vo = new RechargeOrderPageVO();

        vo.setRecords(records);
        vo.setPage(page);
        vo.setSize(size);
        vo.setTotal(total);

        return vo;
    }


    //管理端查询充值订单详情
    @Override
    public RechargeOrder adminGetOrder(Long orderId) {

        RechargeOrder order = rechargeOrderMapper.findById(orderId);

        if (order == null) {
            throw new RuntimeException("充值订单不存在");
        }

        return order;
    }


    //管理端异常订单手动补单
    @Override
    @Transactional
    public void adminCompleteOrder(Long orderId) {

        RechargeOrder order = rechargeOrderMapper.findById(orderId);

        if (order == null) {
            throw new RuntimeException("充值订单不存在");
        }

        //已经支付成功，无需重复补单
        if (RechargeOrderStatus.PAID.getCode().equals(order.getStatus())) {
            return;
        }

        /*
         * 管理员手动补单用于处理异常订单。
         * 允许 PENDING 或 CLOSED 订单被人工确认支付。
         */
        if (!RechargeOrderStatus.PENDING.getCode().equals(order.getStatus()) && !RechargeOrderStatus.CLOSED.getCode().equals(order.getStatus())) {

            throw new RuntimeException("当前订单状态不可手动补单");
        }

        int rows = rechargeOrderMapper.adminUpdatePaid(orderId, RechargeOrderStatus.PENDING.getCode(), RechargeOrderStatus.CLOSED.getCode(), RechargeOrderStatus.PAID.getCode(), LocalDateTime.now());

        if (rows == 0) {

            RechargeOrder latestOrder = rechargeOrderMapper.findById(orderId);

            if (latestOrder != null && RechargeOrderStatus.PAID.getCode().equals(latestOrder.getStatus())) {
                return;
            }

            throw new RuntimeException("手动补单失败");
        }

        //补单成功后补发用户算力
        computePointService.addPoints(order.getUserId(), order.getPoints());
    }

    //生成平台充值订单号
    private String generateOrderNo() {

        String time = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));

        String random = UUID.randomUUID().toString().replace("-", "").substring(0, 12).toUpperCase();

        return "CP" + time + random;
    }
}
