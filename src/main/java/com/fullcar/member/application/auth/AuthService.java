package com.fullcar.member.application.auth;

import com.fullcar.member.presentation.auth.dto.request.AuthRequestDto;
import com.fullcar.member.presentation.auth.dto.response.SocialInfoResponseDto;

public interface AuthService {
    SocialInfoResponseDto getMemberInfo(AuthRequestDto authRequestDto);
}
