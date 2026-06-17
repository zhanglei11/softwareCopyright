package com.sva.common.exception;

public class ServiceException extends RuntimeException {

    private static final long serialVersionUID = 1L;
    private final int code;

    public ServiceException(String message) {
        super(message);
        this.code = 500;
    }

    public ServiceException(int code, String message) {
        super(message);
        this.code = code;
    }

    public int getCode() { return code; }
}
