package com.angu.ai.common.exception;

public class TokenException extends ServiceException {

    public TokenException(String message) {
        super(401, message);
    }
}
