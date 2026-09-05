package com.example.computingpowerrental.vo;

import com.example.computingpowerrental.entity.RechargeOrder;

import java.util.List;

/**
 * @author Lark
 * @ date 2026/9/2  20:55
 * @ description 充值订单分页返回对象
 */
public class RechargeOrderPageVO {
    private List<RechargeOrder> records;

    private Integer page;

    private Integer size;

    private Long total;

    public List<RechargeOrder> getRecords() {
        return records;
    }

    public void setRecords(List<RechargeOrder> records) {
        this.records = records;
    }

    public Integer getPage() {
        return page;
    }

    public void setPage(Integer page) {
        this.page = page;
    }

    public Integer getSize() {
        return size;
    }

    public void setSize(Integer size) {
        this.size = size;
    }

    public Long getTotal() {
        return total;
    }

    public void setTotal(Long total) {
        this.total = total;
    }
}
