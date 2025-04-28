package com.r2s.structure_sample.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.r2s.structure_sample.common.enums.Role;
import com.r2s.structure_sample.common.response.ResponseObject;
import com.r2s.structure_sample.security.JwtFilter;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.nio.file.AccessDeniedException;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {
    private final JwtFilter jwtFilter;

    @Bean
    public WebMvcConfigurer webMvcConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**")
//                        .allowedHeaders("*")
                        .allowedMethods("GET", "POST", "PUT", "DELETE")
                        .allowedOrigins("http://localhost:3000", "http://localhost:8080");
//                        .allowCredentials(true);
            }
        };
    }

    private AccessDeniedHandler accessDeniedHandler() {
        return (request, response, accessDeniedException) -> {
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            var res = ResponseObject.builder()
                    .status(HttpStatus.FORBIDDEN)
                    .message("You don't have permission to access this resource")
                    .build();
            var objectMapper = new ObjectMapper();
            response.setContentType(("application/json"));
            response.getWriter().write(objectMapper.writeValueAsString(res));
        };
    }

    private AuthenticationEntryPoint authenticationEntryPoint() {
        return (request, response, authenticationEntryPointException) -> {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

            var res = ResponseObject.builder()
                    .status(HttpStatus.UNAUTHORIZED)
                    .message("Unauthorized")
                    .build();

            if (authenticationEntryPointException instanceof BadCredentialsException) {
                res.setMessage("Invalid username or password");
            } else if (authenticationEntryPointException instanceof LockedException) {
                res.setMessage("Account is locked");
            } else if (authenticationEntryPointException instanceof DisabledException) {
                res.setMessage("Account is disabled");
            } else if (authenticationEntryPointException instanceof InsufficientAuthenticationException) {
                res.setMessage("Insufficient authentication");
            }

            var objectMapper = new ObjectMapper();
            response.setContentType("application/json");
            response.getWriter().write(objectMapper.writeValueAsString(res));
        };
    }


    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .cors(Customizer.withDefaults())
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/v1/auth/**").permitAll()
                        .requestMatchers("/api/v1/user/**").hasAnyRole(Role.ADMIN.name(), Role.USER.name(), Role.MANAGER.name())
                        .requestMatchers("/api/v1/manager/**").hasAnyRole(Role.ADMIN.name(), Role.MANAGER.name())
                        .requestMatchers("/api/v1/admin/**").hasRole(Role.ADMIN.name())
                        .anyRequest().permitAll()
                )
                .exceptionHandling(ex -> ex
                        .accessDeniedHandler(accessDeniedHandler())
                        .authenticationEntryPoint(authenticationEntryPoint())
                )
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }
}
