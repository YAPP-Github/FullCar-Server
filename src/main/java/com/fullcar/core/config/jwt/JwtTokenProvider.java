package com.fullcar.core.config.jwt;

import com.fullcar.member.domain.member.Member;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;;

import java.security.Key;
import java.util.Date;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtTokenProvider {
    @Value("${jwt.secret}")
    private String secretKey;

    @Value("${jwt.access-token.expire-length}")
    private Long accessTokenExpireLength;

    @Value("${jwt.refresh-token.expire-length}")
    private Long refreshTokenExpireLength;

    private static final String AUTHORIZATION_HEADER = "Authorization";
    private static final String REFRESH_AUTHORIZATION_HEADER = "Refresh";

    public String generateAccessToken(Member member) {
        Date now = new Date();
        Date expiration = new Date(now.getTime() + accessTokenExpireLength);

        final Claims claims = Jwts.claims()
                .setIssuedAt(now)
                .setExpiration(expiration);

        claims.put("memberId", String.valueOf(member.getId().getId()));

        return Jwts.builder()
                .setHeaderParam(Header.TYPE, Header.JWT_TYPE)
                .setClaims(claims)
                .signWith(getSignKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    public String generateRefreshToken() {
        Date now = new Date();
        Date expiration = new Date(now.getTime() + refreshTokenExpireLength);

        final Claims claims = Jwts.claims()
                .setIssuedAt(now)
                .setExpiration(expiration);

        return Jwts.builder()
                .setHeaderParam(Header.TYPE, Header.JWT_TYPE)
                .setClaims(claims)
                .signWith(getSignKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    // 회원 정보 추출
    public Claims getAccessTokenPayload(String token) {
        return Jwts.parserBuilder().setSigningKey(getSignKey()).build().parseClaimsJws(token).getBody();
    }

    // Request Header에서 token 값 가져옴
    public String resolveToken(HttpServletRequest request) {

        String header = request.getHeader(AUTHORIZATION_HEADER);

        if (header == null || !header.startsWith("Bearer ")) {
            return "";
        } else {
            return header.split(" ")[1];
        }
    }

    // 토큰 유효성 검증
    public JwtExceptionType validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(getSignKey()).build().parseClaimsJws(token);
            return JwtExceptionType.VALID_JWT_TOKEN;
        } catch (io.jsonwebtoken.security.SignatureException exception) {
            return JwtExceptionType.INVALID_JWT_SIGNATURE;
        } catch (MalformedJwtException exception) {
            return JwtExceptionType.INVALID_JWT_TOKEN;
        } catch (ExpiredJwtException exception) {
            return JwtExceptionType.EXPIRED_JWT_TOKEN;
        } catch (UnsupportedJwtException exception) {
            return JwtExceptionType.UNSUPPORTED_JWT_TOKEN;
        } catch (IllegalArgumentException exception) {
            return JwtExceptionType.EMPTY_JWT;
        }
    }

    private Key getSignKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
