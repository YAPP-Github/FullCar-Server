package com.fullcar.core.exception;

import com.fullcar.core.response.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class CustomExceptionHandler {

    @ExceptionHandler(CustomException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiResponse<Object> handleCustomException(CustomException e) {

        return ApiResponse.builder()
                .status(e.getStatus().value())
                .message(e.getMessage())
                .build();
    }
}
