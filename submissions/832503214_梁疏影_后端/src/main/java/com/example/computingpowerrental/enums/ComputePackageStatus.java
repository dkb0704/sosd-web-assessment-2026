package com.example.computingpowerrental.enums;

/**
 * @author Lark
 * @ date 2026/8/15  12:15
 * @ description 算力套餐状态
 */
public enum ComputePackageStatus {
    OFF_SHELF(0, "已下架"),

    ON_SHELF(1, "已上架");

    private final Integer code;

    private final String description;

    ComputePackageStatus(
            Integer code,
            String description
    ) {
        this.code = code;
        this.description = description;
    }

    public Integer getCode() {
        return code;
    }

    public String getDescription() {
        return description;
    }
}
