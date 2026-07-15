package com.example.computingpowerrental.exception;

import com.example.computingpowerrental.dto.ApiResponse;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * @author Lark
 * @ date 2026/5/27  19:42
 * @ description 全局异常处理器（统一处理controller、service中抛出的异常）
 */
@RestControllerAdvice
public class GlobalExceptionHandler {
    //处理业务异常
    @ExceptionHandler(RuntimeException.class)
    public ApiResponse<Void> handleRuntimeException(RuntimeException e) {
        return ApiResponse.error(400, e.getMessage());
    }

    //处理参数校验异常
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ApiResponse<Void> handleValidationException(MethodArgumentNotValidException e) {
        String message = e.getBindingResult()
                .getFieldErrors()
                .get(0)
                .getDefaultMessage();

        return ApiResponse.error(400, message);
    }

    //兜底异常处理（避免未处理异常直接暴露给前端）
    @ExceptionHandler(Exception.class)
    public ApiResponse<Void> handleException(Exception e) {
        return ApiResponse.error(500, "服务器内部错误");
    }
}
