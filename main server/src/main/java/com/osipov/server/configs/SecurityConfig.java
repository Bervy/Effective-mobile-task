package com.osipov.server.configs;

import com.osipov.server.filters.JwtAuthFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer.FrameOptionsConfig;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.servlet.util.matcher.MvcRequestMatcher;
import org.springframework.web.servlet.handler.HandlerMappingIntrospector;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {
    private final JwtAuthFilter jwtAuthFilter;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http, HandlerMappingIntrospector introspector) throws Exception {
        MvcRequestMatcher mvcRequestMatcher1 = new MvcRequestMatcher(introspector, "/swagger-ui/**");
        MvcRequestMatcher mvcRequestMatcher2 = new MvcRequestMatcher(introspector, "/swagger-ui.html");
        MvcRequestMatcher mvcRequestMatcher3 = new MvcRequestMatcher(introspector, "/swagger/**");
        MvcRequestMatcher mvcRequestMatcher4 = new MvcRequestMatcher(introspector, "/v3/api-docs/**");
        MvcRequestMatcher mvcRequestMatcher5 = new MvcRequestMatcher(introspector, "/api/auth/**");

        http
                .csrf(AbstractHttpConfigurer::disable)
                .cors(Customizer.withDefaults())
                .authorizeHttpRequests(request -> request
                        .requestMatchers(mvcRequestMatcher1).permitAll()
                        .requestMatchers(mvcRequestMatcher2).permitAll()
                        .requestMatchers(mvcRequestMatcher3).permitAll()
                        .requestMatchers(mvcRequestMatcher4).permitAll()
                        .requestMatchers(mvcRequestMatcher5).permitAll()
                        .anyRequest().authenticated()
                )
                .headers(headers -> headers.frameOptions(FrameOptionsConfig::disable))
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}