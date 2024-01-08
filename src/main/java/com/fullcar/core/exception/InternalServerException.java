package com.fullcar.core.exception;

import com.fullcar.core.response.ErrorCode;
import org.springframework.http.HttpStatus;

public class InternalServerException extends BaseException {
    public InternalServerException(ErrorCode errorCode) {
        super(HttpStatus.INTERNAL_SERVER_ERROR, errorCode.getMessage());
    }
}
