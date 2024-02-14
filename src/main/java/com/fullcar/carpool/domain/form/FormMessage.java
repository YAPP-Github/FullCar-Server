package com.fullcar.carpool.domain.form;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum FormMessage {
    REJECT_MESSAGE("카풀 매칭에 실패했어요. 다른 카풀을 찾아보세요!"),
    REQUEST_TITLE("탑승 요청이 들어왔어요!"),
    REQUEST_BODY("탑승자 정보를 확인하고 승인해 주세요🚘"),
    ACCEPT_TITLE("카풀 매칭에 성공했어요!"),
    ACCEPT_BODY("운전자 정보를 확인해 주세요🚘"),
    REJECT_TITLE("카풀 매칭에 실패했어요."),
    REJECT_BODY("다른 카풀을 찾아볼까요?💁🏻‍♀️");

    private final String message;
}
