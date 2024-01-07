package com.fullcar.core.exception;

import com.fullcar.core.response.ErrorCode;
import org.springframework.http.HttpStatus;

public class NotFoundException extends BaseException {
    public NotFoundException(ErrorCode errorCode) {
        super(HttpStatus.NOT_FOUND, errorCode.getMessage());
    }
}
