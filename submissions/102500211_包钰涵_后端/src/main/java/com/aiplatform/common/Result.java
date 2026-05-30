package com.aiplatform.common;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;

/**
 * 统一响应体
 *
 * @param <T> 响应数据类型
 */
@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Result<T> {

    /** 状态码 */
    private final int code;

    /** 提示信息 */
    private final String message;

    /** 响应数据 */
    private final T data;

    private Result(int code, String message, T data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    // ==================== 成功响应 ====================

    /** 200 - 成功，无 data */
    public static <T> Result<T> success() {
        return new Result<>(200, "success", null);
    }

    /** 200 - 成功，携带 data */
    public static <T> Result<T> success(T data) {
        return new Result<>(200, "success", data);
    }

    /** 200 - 成功，自定义 message + data */
    public static <T> Result<T> success(String message, T data) {
        return new Result<>(200, message, data);
    }

    // ==================== 错误响应 ====================

    /** 自定义状态码 + 错误信息 */
    public static <T> Result<T> error(int code, String message) {
        return new Result<>(code, message, null);
    }

    /** 400 - 参数错误 */
    public static <T> Result<T> badRequest(String message) {
        return new Result<>(400, message, null);
    }

    /** 401 - 未登录 */
    public static <T> Result<T> unauthorized(String message) {
        return new Result<>(401, message, null);
    }

    /** 403 - 无权限 */
    public static <T> Result<T> forbidden(String message) {
        return new Result<>(403, message, null);
    }

    /** 404 - 资源不存在 */
    public static <T> Result<T> notFound(String message) {
        return new Result<>(404, message, null);
    }

    /** 500 - 服务器错误 */
    public static <T> Result<T> serverError(String message) {
        return new Result<>(500, message, null);
    }
}