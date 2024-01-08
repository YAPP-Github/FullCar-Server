package com.fullcar.core.exception;

import com.fullcar.core.response.ErrorCode;
import org.springframework.http.HttpStatus;

public class BadRequestException extends BaseException {
    public BadRequestException(ErrorCode errorCode) {
        super(HttpStatus.BAD_REQUEST, errorCode.getMessage());
    }
}
