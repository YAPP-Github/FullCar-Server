package com.fullcar.core.config.jwt;

import com.fullcar.core.exception.UnauthorizedException;
import com.fullcar.core.response.ErrorCode;
import com.fullcar.member.domain.Member;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final JwtTokenProvider jwtTokenProvider;
    private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
    private static final String RENEW_TOKEN_API_URL = "/api/v1/auth/token";

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws ServletException, IOException {
        try {
            String accessToken = jwtTokenProvider.resolveToken(request);

            if (RENEW_TOKEN_API_URL.equals(request.getRequestURI())) {
                String refreshToken = jwtTokenProvider.resolveRefreshToken(request);

                if (jwtTokenProvider.validateToken(refreshToken) == JwtExceptionType.EMPTY_JWT || jwtTokenProvider.validateToken(accessToken) == JwtExceptionType.EMPTY_JWT) {
                    jwtAuthenticationEntryPoint.setResponse(response, HttpStatus.BAD_REQUEST, ErrorCode.NON_EXISTENT_TOKEN);
                    return;
                } else if (jwtTokenProvider.validateToken(accessToken) == JwtExceptionType.EXPIRED_JWT_TOKEN) {
                    if (jwtTokenProvider.validateToken(refreshToken) == JwtExceptionType.EXPIRED_JWT_TOKEN) {
                        // access, refresh 둘 다 만료
                        jwtAuthenticationEntryPoint.setResponse(response, HttpStatus.UNAUTHORIZED, ErrorCode.SIGNIN_REQUIRED);
                        return;
                    } else if (jwtTokenProvider.validateToken(refreshToken) == JwtExceptionType.VALID_JWT_TOKEN) {
                        // 토큰 재발급
                        Member member = jwtTokenProvider.findMemberByRefreshToken(refreshToken);
                        Authentication authentication = new UserAuthentication(member.getId(), null, null);
                        String newAccessToken = jwtTokenProvider.generateAccessToken(authentication);

                        setAuthentication(newAccessToken);
                        request.setAttribute("newAccessToken", newAccessToken);
                    }
                } else if (jwtTokenProvider.validateToken(accessToken) == JwtExceptionType.VALID_JWT_TOKEN) {
                    // 아직 유효한 accessToken
                    jwtAuthenticationEntryPoint.setResponse(response, HttpStatus.UNAUTHORIZED, ErrorCode.VALID_ACCESS_TOKEN);
                    return;
                } else {
                    throw new UnauthorizedException(ErrorCode.UNAUTHORIZED_TOKEN);
                }
            }
            else {
                JwtExceptionType jwtException = jwtTokenProvider.validateToken(accessToken);

                if (accessToken != null) {
                    // 토큰 검증
                    if (jwtException == JwtExceptionType.VALID_JWT_TOKEN) {
                        setAuthentication(accessToken);
                    }
                }
            }
        } catch (Exception e) {
            throw new UnauthorizedException(ErrorCode.UNAUTHORIZED_TOKEN);
        }
        chain.doFilter(request, response);
    }

    private void setAuthentication(String token) {
        Claims claims = jwtTokenProvider.getAccessTokenPayload(token);
        Authentication authentication = new UserAuthentication(claims.get("memberId"), null, null);
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }
}
