package com.fullcar.member.presentation.auth;

import com.fullcar.core.annotation.CurrentMember;
import com.fullcar.core.response.ApiResponse;
import com.fullcar.core.response.SuccessCode;
import com.fullcar.member.application.auth.AppleAuthService;
import com.fullcar.member.application.auth.AuthServiceProvider;
import com.fullcar.member.application.auth.KakaoAuthService;
import com.fullcar.member.domain.member.Member;
import com.fullcar.member.presentation.auth.dto.request.AppleAuthRequestDto;
import com.fullcar.member.presentation.auth.dto.request.AuthTokenRequestDto;
import com.fullcar.member.presentation.auth.dto.request.KakaoAuthRequestDto;
import com.fullcar.member.presentation.auth.dto.response.AuthResponseDto;
import com.fullcar.member.presentation.auth.dto.response.AuthTokenResponseDto;
import com.fullcar.member.presentation.auth.dto.response.SocialInfoResponseDto;
import com.fullcar.member.presentation.auth.dto.request.WithdrawRequestDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;


@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
@Tag(name = "[Auth] 인증/인가 관련 API")
public class AuthController {
    private final AuthServiceProvider authServiceProvider;
    private final AppleAuthService appleAuthService;
    private final KakaoAuthService kakaoAuthService;

    @Operation(summary = "애플 소셜 로그인 API")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "애플 소셜 로그인 성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "애플 공개키 생성 중 문제 발생", content = @Content),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500", description = "서버 내부 오류", content = @Content)
    })
    @PostMapping("/login/apple")
    public ApiResponse<AuthResponseDto> appleLogin(@RequestBody AppleAuthRequestDto appleAuthRequestDto) throws IOException {
        SocialInfoResponseDto socialResponseDto = appleAuthService.getMemberInfo(appleAuthRequestDto);
        AuthResponseDto responseDto = authServiceProvider.socialLogin(socialResponseDto);

        return ApiResponse.success(SuccessCode.APPLE_LOGIN_SUCCESS, responseDto);
    }

    @Operation(summary = "카카오 소셜 로그인 API")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "카카오 소셜 로그인 성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "유효하지 않은 카카오 토큰", content = @Content),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500", description = "서버 내부 오류", content = @Content)
    })
    @PostMapping("/login/kakao")
    public ApiResponse<AuthResponseDto> kakaoLogin(@RequestBody KakaoAuthRequestDto kakaoAuthRequestDto) {
        SocialInfoResponseDto socialResponseDto = kakaoAuthService.getMemberInfo(kakaoAuthRequestDto);
        AuthResponseDto responseDto = authServiceProvider.socialLogin(socialResponseDto);

        return ApiResponse.success(SuccessCode.KAKAO_LOGIN_SUCCESS, responseDto);
    }

    @Operation(summary = "토큰 재발급 API")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "토큰 재발급 성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "1. access, refreshToken 모두 만료되었습니다. 재로그인이 필요합니다. \t\n 2. 유효하지 않은 토큰 (DB의 refreshToken과 일치하지 않을 경우)", content = @Content),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500", description = "서버 내부 오류", content = @Content)
    })
    @PostMapping("/token")
    @ResponseStatus(HttpStatus.OK)
    public ApiResponse<AuthTokenResponseDto> getNewToken(@RequestBody AuthTokenRequestDto authTokenRequestDto) {
        return ApiResponse.success(SuccessCode.GET_NEW_TOKEN_SUCCESS, authServiceProvider.getNewToken(authTokenRequestDto.getRefreshToken()));
    }

    @Operation(summary = "로그아웃 API")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "로그아웃 성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500", description = "서버 내부 오류", content = @Content)
    })
    @PostMapping("/logout")
    @ResponseStatus(HttpStatus.OK)
    public ApiResponse<Object> socialLogout(@CurrentMember Member member) {
        authServiceProvider.socialLogout(member);
        return ApiResponse.success(SuccessCode.LOGOUT_SUCCESS);
    }

    @Operation(summary = "회원 탈퇴 API")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "탈퇴 성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500", description = "서버 내부 오류", content = @Content)
    })
    @DeleteMapping()
    public ApiResponse<Object> withdrawMember(@CurrentMember Member member, @RequestBody WithdrawRequestDto withdrawRequestDto) throws IOException {
        authServiceProvider.withdrawMember(member, withdrawRequestDto);
        return ApiResponse.success(SuccessCode.WITHDRAW_SUCCESS);
    }
}
