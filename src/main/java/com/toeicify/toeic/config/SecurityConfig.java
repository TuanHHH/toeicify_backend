package com.toeicify.toeic.config;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {
    @Value("${app.client}")
    private String client;
    private final OAuth2LoginSuccessHandler oAuth2LoginSuccessHandler;
    private final CustomAuthenticationEntryPoint customAuthenticationEntryPoint;
    String[] whiteList = {"/",
            "/api/auth/login",
            "/api/auth/refresh",
            "/api/auth/register",
            "/api/auth/register/**",
            "/api/auth/forgot-password",
            "/api/auth/verify-otp",
            "/api/auth/reset-password",
            "/v3/api-docs/**",
            "/login/oauth2/**",
            "/oauth2/**",
            "/login/oauth2/callback/**",
            "/swagger-ui/**",
            "/swagger-ui.html"};

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.cors(Customizer.withDefaults())
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(whiteList).permitAll()

                        .requestMatchers(HttpMethod.GET, "/api/exams", "/api/exams/**", "/api/exams/search").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/exams/search").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/exams").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/api/exams/**").hasRole("ADMIN")

                        .requestMatchers(HttpMethod.GET, "/api/exam-categories", "/api/exam-categories/**").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/exam-categories").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/api/exam-categories/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/api/exam-categories/**").hasRole("ADMIN")

                        .anyRequest().authenticated()
                )
                .formLogin(AbstractHttpConfigurer::disable)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED))
                .oauth2Login(oauth -> oauth
                                .successHandler(oAuth2LoginSuccessHandler)
                                .failureHandler((request, response, exception) -> {
//                            System.err.println("OAuth2 Login Error: " + exception.getMessage());
//                            System.err.println("Request URI: " + request.getRequestURI());
//                            System.err.println("Query String: " + request.getQueryString());
                                    response.sendRedirect(client + "/authentication/error?isLogin=false");
                                })
                )
                .oauth2ResourceServer(oauth2 -> oauth2.jwt(Customizer.withDefaults()).authenticationEntryPoint(customAuthenticationEntryPoint));
        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    //    If a JwtAuthenticationConverter @Bean is defined in the context,
//    it will be automatically used when decoding JWT tokens in the resource server configuration,
//    even if .jwt(jwt -> ...) is not explicitly set.
    @Bean
    public JwtAuthenticationConverter jwtAuthenticationConverter() {
        JwtGrantedAuthoritiesConverter grantedAuthoritiesConverter = new JwtGrantedAuthoritiesConverter();
        grantedAuthoritiesConverter.setAuthoritiesClaimName("authorities");
        grantedAuthoritiesConverter.setAuthorityPrefix("");
        JwtAuthenticationConverter converter = new JwtAuthenticationConverter();
        converter.setJwtGrantedAuthoritiesConverter(grantedAuthoritiesConverter);
        return converter;
    }

}
