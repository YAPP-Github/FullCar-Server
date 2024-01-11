package com.fullcar.member.presentation.dto.request;

import com.fullcar.member.domain.MemberSocialType;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import static lombok.AccessLevel.PROTECTED;

@Data
@Getter
@NoArgsConstructor(access = PROTECTED)
public class AuthRequestDto {

    private MemberSocialType socialType;
    private String token;
    private String fcmToken;
}
