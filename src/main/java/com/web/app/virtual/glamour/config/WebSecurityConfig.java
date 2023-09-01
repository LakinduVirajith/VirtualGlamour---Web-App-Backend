package com.web.app.virtual.glamour.config;

import com.web.app.virtual.glamour.config.jwt.JwtAuthenticationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import static com.web.app.virtual.glamour.enums.Permission.*;
import static com.web.app.virtual.glamour.enums.UserRole.*;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class WebSecurityConfig {

    private static final String[] WHITELIST = {
            "/",
            "/api/v1/user/register",
            "/api/v1/user/activate",
            "/api/v1/user/authenticate",
            "/api/v1/user/refresh-token",
            "/api/v1/user/forgot-password",
            "/api/v1/user/otp-validation",
            "/api/v1/user/rest-password",

            "/v2/api-docs",
            "/v3/api-docs",
            "/v3/api-docs/**",
            "/swagger-resources",
            "/configuration/ui",
            "/configuration/security",
            "/swagger-resources/**",
            "/swagger-ui/**",
            "/webjars/**",
            "/swagger-ui.html"
    };

    private final JwtAuthenticationFilter JwtAuthFilter;
    private final AuthenticationProvider authenticationProvider;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception{
        return http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth -> auth.requestMatchers(WHITELIST).permitAll()
                        .requestMatchers("/api/v1/user/**").hasAnyRole(USER.name(), ADMIN.name())
                        .requestMatchers(HttpMethod.GET, "/api/v1/user/**").hasAnyAuthority(USER_READ.name(), ADMIN_READ.name())
                        .requestMatchers(HttpMethod.POST, "/api/v1/user/**").hasAnyAuthority(USER_CREATE.name(), ADMIN_CREATE.name())
                        .requestMatchers(HttpMethod.PUT, "/api/v1/user/**").hasAnyAuthority(USER_UPDATE.name(), ADMIN_UPDATE.name())
                        .requestMatchers(HttpMethod.DELETE, "/api/v1/user/**").hasAnyAuthority(USER_DELETE.name(), ADMIN_DELETE.name())

                        .requestMatchers("/api/v1/vendor/**").hasAnyRole(VENDOR.name(), ADMIN.name())
                        .requestMatchers(HttpMethod.GET, "/api/v1/vendor/**").hasAnyAuthority(VENDOR_READ.name(), ADMIN_READ.name())
                        .requestMatchers(HttpMethod.POST, "/api/v1/vendor/**").hasAnyAuthority(VENDOR_CREATE.name(), ADMIN_CREATE.name())
                        .requestMatchers(HttpMethod.PUT, "/api/v1/vendor/**").hasAnyAuthority(VENDOR_UPDATE.name(), ADMIN_UPDATE.name())
                        .requestMatchers(HttpMethod.DELETE, "/api/v1/vendor/**").hasAnyAuthority(VENDOR_DELETE.name(), ADMIN_DELETE.name())
                        .anyRequest().authenticated())
                .sessionManagement((session) -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authenticationProvider(authenticationProvider)
                .addFilterBefore(JwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }
}
