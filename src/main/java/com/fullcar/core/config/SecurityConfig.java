package com.fullcar.core.config;

import com.fullcar.core.config.jwt.JwtAuthenticationEntryPoint;
import com.fullcar.core.config.jwt.JwtAuthenticationFilter;
import com.fullcar.core.config.jwt.JwtTokenProvider;
import com.fullcar.member.application.CustomUserDetailService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@RequiredArgsConstructor
@EnableWebSecurity
public class SecurityConfig {
    private final JwtTokenProvider jwtTokenProvider;
    private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
    private final CustomUserDetailService customUserDetailService;

    private static final String[] AUTH_WHITELIST = {
            "/api/v1/auth/**",
            "/api/v1/test/**",

            "/configuration/ui",
            "/configuration/security",
            "/webjars/**",
            "/swagger/**",
            "/swagger-resources/**",
            "/swagger-ui/**",
            "/v3/api-docs/**",

            "/health-check"
    };

    @Bean
    protected SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .httpBasic(AbstractHttpConfigurer::disable)
                .csrf(AbstractHttpConfigurer::disable)
                .formLogin(AbstractHttpConfigurer::disable)
                .cors(AbstractHttpConfigurer::disable)
                .sessionManagement((sessionManagement) ->
                        sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                .authorizeHttpRequests((authorizeRequests) -> {
                    authorizeRequests.requestMatchers(AUTH_WHITELIST).permitAll();
                    authorizeRequests.anyRequest().authenticated();
                })
                .exceptionHandling(handler -> handler.authenticationEntryPoint(jwtAuthenticationEntryPoint))
                .addFilterBefore(new JwtAuthenticationFilter(jwtTokenProvider, jwtAuthenticationEntryPoint, customUserDetailService), UsernamePasswordAuthenticationFilter.class);

            return http.build();
        }
}
