package com.fullcar.core.config.jwt;

import com.fullcar.core.exception.UnauthorizedException;
import com.fullcar.core.response.ErrorCode;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final JwtTokenProvider jwtTokenProvider;
    private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws ServletException, IOException {
        try {
            String accessToken = jwtTokenProvider.resolveToken(request);
            JwtExceptionType jwtException = jwtTokenProvider.validateToken(accessToken);

            if (accessToken != null) {
                // 토큰 검증
                if (jwtException == JwtExceptionType.VALID_JWT_TOKEN) {
                    setAuthentication(accessToken);
                }
            }
        } catch (Exception e) {
            throw new UnauthorizedException(ErrorCode.UNAUTHORIZED_TOKEN);
        }
        chain.doFilter(request, response);
    }

    private void setAuthentication(String token) {
        Claims claims = jwtTokenProvider.getAccessTokenPayload(token);
        Authentication authentication = new UserAuthentication(Long.valueOf(String.valueOf(claims.get("memberId"))), null, null);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        System.out.println(Long.valueOf(String.valueOf(claims.get("memberId"))));
    }
}
