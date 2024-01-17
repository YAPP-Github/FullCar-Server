package com.fullcar.core.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import static org.springframework.http.HttpStatus.*;

@Getter
@AllArgsConstructor
public enum SuccessCode {
    /* 201 CREATED */

    /* 200 OK */
    SIGNIN_SUCCESS(OK, "소셜로그인 성공"),
    REGISTER_SUCCESS(OK, "등록 성공");

    private final HttpStatus status;
    private final String message;
}
