package com.kimtaehoondev.board.config;

import com.kimtaehoondev.board.filter.JwtAuthenticationFilter;
import com.kimtaehoondev.board.auth.jwt.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class WebSecurityConfig {
    private final JwtTokenProvider jwtTokenProvider;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
            .httpBasic(AbstractHttpConfigurer::disable)
            .csrf(csrf -> csrf.disable()) // CSRF 보호 비활성화
            .sessionManagement(x -> x.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .authorizeRequests(x -> {
                x.mvcMatchers(HttpMethod.POST, "/api/posts").hasRole("USER")
                    .mvcMatchers(HttpMethod.PUT, "/api/posts/**").hasRole("USER")
                    .mvcMatchers(HttpMethod.DELETE, "/api/posts/**").hasRole("USER")
                    .anyRequest().permitAll();
            })
            .exceptionHandling(
                x -> x.authenticationEntryPoint(((request, response, authException) -> {
                    response.setStatus(HttpStatus.UNAUTHORIZED.value());
                })))
            .addFilterBefore(new JwtAuthenticationFilter(jwtTokenProvider),
                UsernamePasswordAuthenticationFilter.class)
            .build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }
}
