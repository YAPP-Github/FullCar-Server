package com.fullcar.member.application;

import com.fullcar.member.presentation.dto.request.AuthRequestDto;
import com.fullcar.member.presentation.dto.response.SocialInfoResponseDto;

public interface AuthService {
    SocialInfoResponseDto getMemberInfo(AuthRequestDto authRequestDto);
}
