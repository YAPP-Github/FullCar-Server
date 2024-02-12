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
    FAILED_TO_GENERATE_APPLE_TOKEN(BAD_REQUEST, "애플 access Token 생성 중 문제 발생"),
    EMAIL_ADDRESS_IN_BLACKLIST(BAD_REQUEST, "블랙리스트에 있는 이메일 주소입니다."),
    CANNOT_SEND_TO_OWN_CARPOOL(BAD_REQUEST, "자기자신의 카풀에는 신청할 수 없습니다."),
    DUPLICATED_FORM(BAD_REQUEST, "이미 요청을 보낸 카풀입니다."),
    DUPLICATED_NICKNAME(BAD_REQUEST, "중복된 닉네임 입니다."),
    EXISTED_CAR_IN_MEMBER(BAD_REQUEST, "이미 차량이 등록되었습니다."),
    CANNOT_CHANGE_FORM_STATE(BAD_REQUEST, "카풀에 등록된 운전자만 신청서 수락/거절을 할 수 있습니다."),
    INVALID_FORM_STATE(BAD_REQUEST, "유효하지 않은 신청서 상태입니다."),
    EXISTED_CODE_IN_MAIL(BAD_REQUEST, "이미 인증번호를 보냈습니다."),
    NOT_MATCHED_CODE(BAD_REQUEST, "인증번호가 일치하지 않습니다."),
    INVALID_SOCIAL_TYPE(BAD_REQUEST, "유효하지 않은 소셜 로그인 타입 입니다."),

    /* 401 UNAUTHORIZED */
    UNAUTHORIZED_KAKAO_TOKEN(UNAUTHORIZED, "유효하지 않은 카카오 토큰"),
    UNAUTHORIZED_TOKEN(UNAUTHORIZED,"유효하지 않은 토큰"),
    EXPIRED_TOKEN(UNAUTHORIZED, "만료된 accessToken 입니다."),
    INVALID_CLAIMS(UNAUTHORIZED, "올바르지 않은 Claim"),
    SIGNIN_REQUIRED(UNAUTHORIZED, "access, refreshToken 모두 만료되었습니다. 재로그인이 필요합니다."),
    INVALID_MEMBER(UNAUTHORIZED, "유효하지 않은 유저"),
    INVALID_KAKAO_USER(UNAUTHORIZED, "이미 탈퇴되었거나 유효하지 않은 카카오 유저입니다."),

    /* 404 NOT FOUND */
    NOT_EXIST_USER(NOT_FOUND, "존재하지 않는 사용자입니다."),
    NOT_EXIST_CARPOOL(NOT_FOUND, "존재하지 않는 카풀입니다."),
    NOT_EXIST_CAR(NOT_FOUND, "존재하지 않는 차량입니다."),
    NOT_EXIST_FORM(NOT_FOUND, "존재하지 않는 신청서입니다."),

    /* 500 INTERNAL SERVER ERROR */
    FAILED_TO_SEND_NOTIFICATION(INTERNAL_SERVER_ERROR, "푸시알림 전송 실패");

    private final HttpStatus status;
    private final String message;
}
