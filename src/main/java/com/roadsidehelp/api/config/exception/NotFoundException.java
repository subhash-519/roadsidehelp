package com.roadsidehelp.api.config.exception;

import org.springframework.http.HttpStatus;

public class NotFoundException extends ApiException {
    public NotFoundException(String message) {
        super(ErrorCode.USER_NOT_FOUND, message, HttpStatus.NOT_FOUND);
    }
}
