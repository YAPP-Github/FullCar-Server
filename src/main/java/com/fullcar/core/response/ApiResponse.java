package com.fullcar.core.response;

import lombok.Builder;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@Builder
public class ApiResponse<T> {
    private final int status;
    private final String message;
    private T data;

    private ApiResponse(int status, String message, T data) {
        this.status = status;
        this.message = message;
        this.data = data;
    }

    private ApiResponse(int status, String message) {
        this.status = status;
        this.message = message;
    }

    public static <T> ApiResponse<T> success(SuccessCode code, T data) {
        return new ApiResponse<>(code.getStatus().value(), code.getMessage(), data);
    }

    public static <T> ApiResponse<T> success(SuccessCode code) {
        return new ApiResponse<>(code.getStatus().value(), code.getMessage());
    }

    public static <T> ApiResponse<T> error(ErrorCode code) {
        return new ApiResponse<>(code.getStatus().value(), code.getMessage());
    }

    public static <T> ApiResponse<T> error(HttpStatus status, String message) {
        return new ApiResponse<>(status.value(), message);
    }
}
