package com.fullcar.member.presentation;

import com.fullcar.core.response.ApiResponse;
import com.fullcar.core.response.SuccessCode;
import com.fullcar.member.application.AuthService;
import com.fullcar.member.application.AuthServiceProvider;
import com.fullcar.member.presentation.dto.request.AuthRequestDto;
import com.fullcar.member.presentation.dto.request.AuthTokenRequestDto;
import com.fullcar.member.presentation.dto.response.AuthResponseDto;
import com.fullcar.member.presentation.dto.response.AuthTokenResponseDto;
import com.fullcar.member.presentation.dto.response.SocialInfoResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthServiceProvider authServiceProvider;

    @PostMapping()
    public ApiResponse<AuthResponseDto> socialLogin(@RequestBody AuthRequestDto authRequestDto) {

        AuthService authService = authServiceProvider.getAuthService(authRequestDto.getSocialType());
        SocialInfoResponseDto socialResponseDto = authService.getMemberInfo(authRequestDto);
        AuthResponseDto responseDto = authServiceProvider.socialLogin(socialResponseDto);

        return ApiResponse.success(SuccessCode.SIGNIN_SUCCESS, responseDto);
    }

    @GetMapping("/token")
    @ResponseStatus(HttpStatus.OK)
    public ApiResponse<AuthTokenResponseDto> getNewToken(@RequestBody AuthTokenRequestDto authTokenRequestDto) {
        return ApiResponse.success(SuccessCode.GET_NEW_TOKEN_SUCCESS, authServiceProvider.getNewToken(authTokenRequestDto.getRefreshToken()));
    }
}
