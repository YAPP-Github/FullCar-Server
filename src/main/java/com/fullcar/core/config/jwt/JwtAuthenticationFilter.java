package com.fullcar.core.config.jwt;

import com.fullcar.core.exception.UnauthorizedException;
import com.fullcar.core.response.ErrorCode;
import com.fullcar.member.application.auth.CustomUserDetailService;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final JwtTokenProvider jwtTokenProvider;
    private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
    private final CustomUserDetailService customUserDetailService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws ServletException, IOException {
        try {
            String accessToken = jwtTokenProvider.resolveToken(request);
            JwtExceptionType jwtException = jwtTokenProvider.validateToken(accessToken);

            if (accessToken != null) {
                // 토큰 검증
                if (jwtException == JwtExceptionType.VALID_JWT_TOKEN) {
                    setAuthentication(accessToken);
                } else if (jwtException == JwtExceptionType.EXPIRED_JWT_TOKEN) {
                    jwtAuthenticationEntryPoint.setResponse(response, HttpStatus.UNAUTHORIZED, ErrorCode.EXPIRED_TOKEN);
                    return;
                }
            }
        } catch (Exception e) {
            throw new UnauthorizedException(ErrorCode.UNAUTHORIZED_TOKEN);
        }
        chain.doFilter(request, response);
    }

    private void setAuthentication(String token) {
        Claims claims = jwtTokenProvider.getAccessTokenPayload(token);
        UserDetails userDetails = customUserDetailService.loadUserByUsername((String) claims.get("memberId"));
        Authentication authentication = new UserAuthentication(userDetails, null, userDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }
}
