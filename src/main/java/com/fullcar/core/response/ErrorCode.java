package com.fullcar.core.response;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

import static org.springframework.http.HttpStatus.*;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public enum ErrorCode {

    /* 400 BAD REQUEST */
    FAILED_TO_GENERATE_PUBLIC_KEY(BAD_REQUEST, "애플 공개키 생성 중 문제 발생"),

    /* 401 UNAUTHORIZED */
    UNAUTHORIZED_KAKAO_TOKEN(UNAUTHORIZED, "유효하지 않은 카카오 토큰"),
    UNAUTHORIZED_TOKEN(UNAUTHORIZED,"유효하지 않은 토큰"),
    EXPIRED_TOKEN(UNAUTHORIZED, "accessToken이 만료되었습니다."),
    INVALID_CLAIMS(UNAUTHORIZED, "올바르지 않은 Claim"),
    SIGNIN_REQUIRED(UNAUTHORIZED, "access, refreshToken 모두 만료되었습니다. 재로그인이 필요합니다."),
    INVALID_MEMBER(UNAUTHORIZED, "유효하지 않은 유저"),

    /* 404 NOT FOUND */
    NOT_FOUND_MEMBER(BAD_REQUEST, "존재하지 않는 유저");

    private final HttpStatus status;
    private final String message;
}
