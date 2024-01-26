package com.fullcar.carpool.domain.event;

import com.fullcar.member.domain.member.MemberId;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class MemberRequestEvent {
    private MemberId memberId;
}
