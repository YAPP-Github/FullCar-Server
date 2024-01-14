package com.fullcar.member.presentation.dto.request;

import com.fullcar.member.domain.MemberSocialType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import static lombok.AccessLevel.PROTECTED;

@Data
@Getter
@NoArgsConstructor(access = PROTECTED)
public class AuthRequestDto {

    @Schema(description = "소셜로그인 타입")
    private MemberSocialType socialType;

    @Schema(description = "소셜로그인 토큰", example = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJpYXQiOjE3MDUyMzQ2NjMsImV4cCI6MTcwNTIzNDg0MywibWVtYmVySWQiOiJjb20uZnVsbGNhci5tZW1iZXIuZG9tYWluLk1lbWJlcklkQDQ5ODRlODM3In0.Qf8uXvaWR9K_pD4jgZlnkFpcx2-zlRKENXJhGXkMGSs")
    private String token;

    @Schema(description = "디바이스 토큰")
    private String deviceToken;
}
