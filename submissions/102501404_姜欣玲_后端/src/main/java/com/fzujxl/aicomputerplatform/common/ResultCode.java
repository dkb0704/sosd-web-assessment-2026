package com.fzujxl.aicomputerplatform.common;

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

    private ResultCode() {

    }
}
