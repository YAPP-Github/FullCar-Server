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
    APPLE_LOGIN_SUCCESS(OK, "애플 소셜 로그인 성공"),
    KAKAO_LOGIN_SUCCESS(OK, "카카오 소셜 로그인 성공"),
    GET_NEW_TOKEN_SUCCESS(OK, "토큰 재발급 성공"),
    READ_SUCCESS(OK, "조회 성공"),
    EMAIL_SENT_SUCCESS(OK, "인증메일 발송 성공"),
    LOGOUT_SUCCESS(OK, "로그아웃 성공"),
    AVAILABLE_NICKNAME(OK, "사용 가능한 닉네임"),
    UPDATE_SUCCESS(OK, "수정 성공"),
    CODE_VERIFICATION_SUCCESS(OK, "인증 성공"),
    WITHDRAW_SUCCESS(OK, "탈퇴 성공"),
    SAVE_DEVICE_TOKEN_SUCCESS(OK, "디바이스 토큰 등록 성공");

    private final HttpStatus status;
    private final String message;
}
