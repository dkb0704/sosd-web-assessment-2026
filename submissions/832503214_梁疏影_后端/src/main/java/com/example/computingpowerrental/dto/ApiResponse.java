package com.example.computingpowerrental.dto;

import lombok.Data;

/**
 * @author Lark
 * @ date 2026/5/25  14:17
 * @ description 统一接口返回对象
 */
@Data
public class ApiResponse<T> {

    private Integer code;

    //返回消息
    private String message;

    //返回数据
    private T data;

    public ApiResponse() {
    }

    public ApiResponse(Integer code, String message, T data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    //成功返回（带数据）
    public static <T> ApiResponse<T> success(String message, T data) {
        return new ApiResponse<>(200, message, data);
    }

    //成功返回（无数据）
    public static <T> ApiResponse<T> success(String message) {
        return new ApiResponse<>(200, message, null);
    }

    //错误返回
    public static <T> ApiResponse<T> error(Integer code, String message) {
        return new ApiResponse<>(code, message, null);
    }
}
