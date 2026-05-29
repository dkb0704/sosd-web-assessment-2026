package com.fzujxl.aicomputerplatform.exception;

import com.fzujxl.aicomputerplatform.common.ResultCode;
import lombok.Getter;

@Getter
public class BusinessException extends RuntimeException {
    private final Integer code;

    public BusinessException(String message) {
        super(message);
        this.code = ResultCode.BAD_REQUEST;
    }

    public BusinessException(Integer code, String message) {
        super(message);
        this.code = code;
    }

    public BusinessException(String message, Throwable cause) {
        super(message, cause);
        this.code = ResultCode.BAD_REQUEST;
    }
}
