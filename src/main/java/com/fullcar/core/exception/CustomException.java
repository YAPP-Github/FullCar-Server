package com.fullcar.core.exception;

import com.fullcar.core.response.ErrorCode;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CustomException extends RuntimeException {
    HttpStatus status;
    String message;

    public CustomException(ErrorCode code) {
        super(String.valueOf(code));
        this.status = code.getStatus();
        this.message = code.getMessage();
    }
}
