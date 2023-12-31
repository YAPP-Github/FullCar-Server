package com.fullcar.member.presentation;

import com.fullcar.core.response.ApiResponse;
import com.fullcar.core.response.SuccessCode;
import com.fullcar.member.application.AuthService;
import com.fullcar.member.application.AuthServiceProvider;
import com.fullcar.member.presentation.dto.request.AuthRequestDto;
import com.fullcar.member.presentation.dto.response.AuthResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthServiceProvider authServiceProvider;

    @PostMapping()
    public ApiResponse<AuthResponseDto> socialLogin(@RequestBody AuthRequestDto authRequestDto) {

        AuthService authService = authServiceProvider.getAuthService(authRequestDto.getSocialType());
        AuthResponseDto responseDto = authService.socialLogin(authRequestDto);

        return ApiResponse.success(SuccessCode.SIGNIN_SUCCESS, responseDto);
    }
}
