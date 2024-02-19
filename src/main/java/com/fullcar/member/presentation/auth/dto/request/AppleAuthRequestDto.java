package com.fullcar.member.presentation.auth.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import static lombok.AccessLevel.PROTECTED;

@Data
@Getter
@NoArgsConstructor(access = PROTECTED)
public class AppleAuthRequestDto {

    @Schema(description = "authorization_code")
    private String authCode;

    @Schema(description = "id_token", example = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJpYXQiOjE3MDUyMzQ2NjMsImV4cCI6MTcwNTIzNDg0MywibWVtYmVySWQiOiJjb20uZnVsbGNhci5tZW1iZXIuZG9tYWluLk1lbWJlcklkQDQ5ODRlODM3In0.Qf8uXvaWR9K_pD4jgZlnkFpcx2-zlRKENXJhGXkMGSs")
    private String idToken;
}
