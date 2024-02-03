package com.fullcar.member.presentation.member;

import com.fullcar.core.annotation.CurrentMember;
import com.fullcar.core.response.ApiResponse;
import com.fullcar.core.response.SuccessCode;
import com.fullcar.member.application.member.MemberService;
import com.fullcar.member.domain.member.Member;
import com.fullcar.member.presentation.member.dto.request.CompanyRequestDto;
import com.fullcar.member.presentation.member.dto.request.EmailRequestDto;
import com.fullcar.member.presentation.member.dto.response.MemberGetResponseDto;
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

    @Operation(summary = "회사 선택 API")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "201", description = "등록 성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500", description = "서버 내부 오류", content = @Content)
    })
    @PostMapping("/onboarding/company")
    public ApiResponse<Object> postCompany(@CurrentMember Member member, @RequestBody @Valid CompanyRequestDto companyRequestDto) {
        memberService.registerCompany(member, companyRequestDto);
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
    public ApiResponse<Object> sendAuthenticationMail(@RequestBody EmailRequestDto emailRequestDto) {
        memberService.sendMail(emailRequestDto);
        return ApiResponse.success(SuccessCode.EMAIL_SENT_SUCCESS);
    }
}
