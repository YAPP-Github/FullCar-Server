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

    /* 401 UNAUTHORIZED */
    UNAUTHORIZED_KAKAO_TOKEN(UNAUTHORIZED, "유효하지 않은 카카오 토큰"),
    UNAUTHORIZED_TOKEN(UNAUTHORIZED,"유효하지 않은 토큰"),
    /* 404 NOT FOUND */
    NOT_FOUND_MEMBER(BAD_REQUEST, "존재하지 않는 유저");

    private final HttpStatus status;
    private final String message;
}
