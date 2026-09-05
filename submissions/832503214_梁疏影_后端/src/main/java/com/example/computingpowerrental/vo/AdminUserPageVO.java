package com.example.computingpowerrental.vo;

import com.example.computingpowerrental.dto.UserInfoResponse;

import java.util.List;

/**
 * @author Lark
 * @ date 2026/9/2  22:43
 * @ description 管理端用户分页返回对象
 */
public class AdminUserPageVO {
    private List<UserInfoResponse> records;
    private Integer page;
    private Integer size;
    private Long total;

    public List<UserInfoResponse> getRecords() {
        return records;
    }

    public void setRecords(List<UserInfoResponse> records) {
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
