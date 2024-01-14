package com.fullcar.core.exception;

import com.fullcar.core.response.ApiResponse;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class CustomExceptionHandler {

    @ExceptionHandler(CustomException.class)
    public ApiResponse<Object> handleCustomException(CustomException e) {

        return ApiResponse.builder()
                .status(e.getStatus().value())
                .message(e.getMessage())
                .build();
    }
}
