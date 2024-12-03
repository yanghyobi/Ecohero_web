package com.example.ecohero.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers("/user/signup", "/user/login", "/css/**", "/js/**").permitAll()
                        .requestMatchers("/user/logout", "/user/{id}", "/comments/**", "/mypage", "/uploads/**").authenticated()
                        .requestMatchers("/user/check-username", "/images/**").permitAll()
                        .requestMatchers("/posts").permitAll()
                        .requestMatchers(HttpMethod.POST, "/posts/*/like").authenticated()
                        .anyRequest().permitAll()
                )
                .formLogin(form -> form
                        .loginPage("/user/login")
                        .loginProcessingUrl("/user/login")
                        .successHandler((request, response, authentication) -> {
                            String redirectUrl = request.getParameter("redirectUrl");
                            if (redirectUrl != null) {
                                response.sendRedirect(redirectUrl);
                            } else {
                                response.sendRedirect("/home");
                            }
                        })
                        .failureUrl("/user/login?error=true")
                        .permitAll()
                )
                .logout(logout -> logout
                        .logoutUrl("/user/logout") // 로그아웃 URL
                        .logoutSuccessUrl("/home") // 로그아웃 후 리다이렉트
                        .invalidateHttpSession(true) // 세션 무효화
                        .deleteCookies("JSESSIONID") // 쿠키 삭제
                        .permitAll()
                )
                .sessionManagement(session -> session
                        .maximumSessions(1)
                        .maxSessionsPreventsLogin(false)
                )
                .csrf(csrf -> csrf
                        .ignoringRequestMatchers("/user/login", "/user/signup", "/user/logout", "/mypage/update")
                        .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse()) // CSRF 토큰을 쿠키로 저장
                );
        return http.build();
    }

    // 비밀번호 암호화(사용자 인증 강화)
    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }
}
