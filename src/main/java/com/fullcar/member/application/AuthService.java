package com.fullcar.member.application;

import com.fullcar.member.presentation.dto.request.AuthRequestDto;
import com.fullcar.member.presentation.dto.response.AuthResponseDto;

public interface AuthService {
    AuthResponseDto socialLogin(AuthRequestDto authRequestDto);
}
