package com.ddokdoghotdog.gowalk.global.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer.FrameOptionsConfig;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.ddokdoghotdog.gowalk.global.jwt.TokenAuthenticationFilter;
import com.ddokdoghotdog.gowalk.global.jwt.TokenExceptionFilter;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Configuration
@EnableWebSecurity
public class SecurityConfig {

        private final CustomOAuth2UserService oAuth2UserService;
        private final OAuth2SuccessHandler oAuth2SuccessHandler;
        private final TokenAuthenticationFilter tokenAuthenticationFilter;

        @Bean
        public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
                http
                                // rest api 설정
                                .csrf(AbstractHttpConfigurer::disable) // csrf 비활성화 -> cookie를 사용하지 않으면 꺼도 된다. (cookie를
                                                                       // 사용할 경우
                                                                       // httpOnly(XSS 방어), sameSite(CSRF 방어)로 방어해야 한다.)
                                .cors(AbstractHttpConfigurer::disable) // cors 비활성화 -> 프론트와 연결 시 따로 설정 필요
                                .httpBasic(AbstractHttpConfigurer::disable) // 기본 인증 로그인 비활성화
                                .formLogin(AbstractHttpConfigurer::disable) // 기본 login form 비활성화
                                .logout(AbstractHttpConfigurer::disable) // 기본 logout 비활성화
                                .headers(c -> c.frameOptions(
                                                FrameOptionsConfig::disable).disable()) // X-Frame-Options 비활성화
                                .sessionManagement(c -> c.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

                                .authorizeHttpRequests(authorize -> authorize
                                                .requestMatchers("/auth/success").permitAll()
                                                .requestMatchers("/auth/register").permitAll()
                                                .requestMatchers("/error", "/favicon.ico").permitAll()
                                                .requestMatchers("/", "/hc", "/env").permitAll()
                                                .requestMatchers("/swagger", "/swagger-ui.html", "/swagger-ui/**", "/api-docs", "/api-docs/**", "/v3/api-docs/**").permitAll()
                                                .requestMatchers("/api/shop/payments/approve/**", "/api/shop/payments/cancel/**", "/api/shop/payments/fail/**").permitAll()
                                                // .requestMatchers("api/mypage/pets").hasRole("ADMIN")
                                                // .anyRequest().authenticated())
                                                .anyRequest().permitAll())


                                .oauth2Login(oauth -> oauth.userInfoEndpoint(c -> c.userService(oAuth2UserService))
                                                .successHandler(oAuth2SuccessHandler)
                                                .failureHandler(new OAuth2FailureHandler()))

                                .addFilterBefore(tokenAuthenticationFilter,
                                                UsernamePasswordAuthenticationFilter.class)
                                .addFilterBefore(new TokenExceptionFilter(), tokenAuthenticationFilter.getClass())

                                .exceptionHandling((exceptions) -> exceptions
                                                .authenticationEntryPoint(new CustomAuthenticationEntryPoint())
                                                .accessDeniedHandler(new CustomAccessDeniedHandler()));

                return http.build();
        }
}
