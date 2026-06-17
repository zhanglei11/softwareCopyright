package com.sursoft.iidp.common.exception;

import com.sursoft.iidp.common.constant.HttpStatus;
import lombok.Getter;

@Getter
public class BusinessException extends RuntimeException {
    private final int code;

    public BusinessException(String message) {
        super(message);
        this.code = HttpStatus.ERROR;
    }

    public BusinessException(int code, String message) {
        super(message);
        this.code = code;
    }
}
