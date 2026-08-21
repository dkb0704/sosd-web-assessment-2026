package com.fzujxl.aicomputerplatform.common;

import lombok.Getter;

@Getter
public class ResultCode {

    public static final Integer SUCCESS = 200;

    public static final Integer BAD_REQUEST = 400;
    // 未认证
    public static  final Integer UNAUTHORIZED = 401;
    // 未授权
    public static  final Integer FORBIDDEN = 403;
    // 资源不存在
    public static  final Integer NOT_FOUND = 404;

    public static final Integer INTERNAL_ERROR = 500;

    private final Integer code;
    private final String message;

    private ResultCode(Integer code, String message) {
        this.code = code;
        this.message = message;
    }
}
