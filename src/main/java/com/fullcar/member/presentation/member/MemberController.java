package com.fullcar.member.presentation.member;

import com.fullcar.core.annotation.CurrentMember;
import com.fullcar.core.response.ApiResponse;
import com.fullcar.core.response.SuccessCode;
import com.fullcar.member.application.member.MemberService;
import com.fullcar.member.domain.member.Member;
import com.fullcar.member.domain.member.service.MailService;
import com.fullcar.member.presentation.member.dto.request.CodeRequestDto;
import com.fullcar.member.presentation.member.dto.request.NicknameRequestDto;
import com.fullcar.member.presentation.member.dto.request.OnboardingRequestDto;
import com.fullcar.member.presentation.member.dto.request.EmailRequestDto;
import com.fullcar.member.presentation.member.dto.response.MemberGetResponseDto;
import com.fullcar.member.presentation.member.dto.response.NicknameResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/members")
@Tag(name = "[Member] 멤버 관련 API")
public class MemberController {

    private final MemberService memberService;
    private final MailService mailService;

    @Operation(summary = "온보딩 API")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "201", description = "등록 성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500", description = "서버 내부 오류", content = @Content)
    })
    @PostMapping("/onboarding")
    public ApiResponse<Object> postOnboarding(@CurrentMember Member member, @RequestBody @Valid OnboardingRequestDto onboardingRequestDto) {
        memberService.registerOnboarding(member, onboardingRequestDto);
        return ApiResponse.success(SuccessCode.REGISTER_SUCCESS);
    }

    @Operation(summary = "회원 정보 조회 API")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "조회 성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500", description = "서버 내부 오류", content = @Content)
    })
    @GetMapping()
    public ApiResponse<MemberGetResponseDto> getMember(@CurrentMember Member member) {
        MemberGetResponseDto response = memberService.getMember(member);
        return ApiResponse.success(SuccessCode.READ_SUCCESS, response);
    }

    @Operation(summary = "회사 메일 인증 API")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "인증메일 발송 성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500", description = "서버 내부 오류", content = @Content)
    })
    @PostMapping("/onboarding/company/email")
    public ApiResponse<Object> sendAuthenticationMail(@CurrentMember Member member, @RequestBody EmailRequestDto emailRequestDto) {
        mailService.sendMail(member, emailRequestDto);
        return ApiResponse.success(SuccessCode.EMAIL_SENT_SUCCESS);
    }

    @Operation(summary = "닉네임 중복 검사 API")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "사용 가능한 닉네임"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500", description = "서버 내부 오류", content = @Content)
    })
    @PostMapping("/onboarding/nickname")
    public ApiResponse<NicknameResponseDto> checkNicknameDuplication(@RequestBody NicknameRequestDto nicknameRequestDto) {
        NicknameResponseDto responseDto = memberService.checkNicknameDuplication(nicknameRequestDto);
        return ApiResponse.success(SuccessCode.AVAILABLE_NICKNAME, responseDto);
    }

    @Operation(summary = "인증번호 인증 API")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "인증 성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500", description = "서버 내부 오류", content = @Content)
    })
    @PostMapping("/onboarding/company/email/check")
    public ApiResponse<Object> checkMailAuthenticationCode(@CurrentMember Member member, @RequestBody CodeRequestDto codeRequestDto) {
        mailService.checkMailAuthenticationCode(member, codeRequestDto);
        return ApiResponse.success(SuccessCode.CODE_VERIFICATION_SUCCESS);
    }
}
