package com.fullcar.core.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import static org.springframework.http.HttpStatus.*;

@Getter
@AllArgsConstructor
public enum SuccessCode {
    /* 201 CREATED */
    REGISTER_SUCCESS(CREATED, "등록 성공"),

    /* 200 OK */
    SIGNIN_SUCCESS(OK, "소셜로그인 성공"),
    GET_NEW_TOKEN_SUCCESS(OK, "토큰 재발급 성공"),
    READ_SUCCESS(OK, "조회 성공"),
    EMAIL_SENT_SUCCESS(OK, "인증메일 발송 성공");

    private final HttpStatus status;
    private final String message;
}
