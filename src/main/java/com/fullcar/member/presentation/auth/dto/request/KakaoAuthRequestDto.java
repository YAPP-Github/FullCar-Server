package com.fullcar.member.presentation.auth.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import static lombok.AccessLevel.PROTECTED;

@Data
@Getter
@NoArgsConstructor(access = PROTECTED)
public class KakaoAuthRequestDto {

    @Schema(description = "카카오에서 받은 액세스 토큰", example = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJpYXQiOjE3MDUyMzQ2NjMsImV4cCI6MTcwNTIzNDg0MywibWVtYmVySWQiOiJjb20uZnVsbGNhci5tZW1iZXIuZG9tYWluLk1lbWJlcklkQDQ5ODRlODM3In0.Qf8uXvaWR9K_pD4jgZlnkFpcx2-zlRKENXJhGXkMGSs")
    private String token;
}
