package com.cil.bf.gestion_courriers_back.Auth.security;

import java.util.Arrays;
import java.util.Collections;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.session.SessionManagementFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import com.cil.bf.gestion_courriers_back.Auth.jwt.JWTAuthenticationFilter;
import com.cil.bf.gestion_courriers_back.Auth.service.Impl.CustomerUserDetailsService;

import lombok.RequiredArgsConstructor;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class securityConfig {

        private final CustomerUserDetailsService customerUserDetailsService;

        private final JWTAuthenticationFilter jwtAuthenticationFilter;

        protected void configure(AuthenticationManagerBuilder auth) throws Exception {
                auth.userDetailsService(customerUserDetailsService)
                                .passwordEncoder(passwordEncoder());
        }

        @Bean
        public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
                http.addFilterBefore(corsFilter(), SessionManagementFilter.class)
                                .cors((cors) -> cors
                                                .configurationSource((request) -> new CorsConfiguration()
                                                                .applyPermitDefaultValues()))
                                .csrf((csrf) -> csrf.disable())
                                .authorizeHttpRequests((authorizeRequests) -> authorizeRequests
                                                .requestMatchers(

                                                                "/v2/api-docs",
                                                                "/swagger-resources",
                                                                "/swagger-resources/**",
                                                                "/configuration/ui",
                                                                "/configuration/security",
                                                                "/swagger-ui.html",
                                                                "/swagger-ui/**",
                                                                "/webjars/**",
                                                                "/v3/api-docs/**",
                                                                "/auth/users/sign-in",
                                                                "/users/signup",
                                                                "/**")
                                                .permitAll()
                                                .anyRequest().authenticated())

                                .sessionManagement((session) -> session
                                                .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                                .authenticationProvider(authenticationProvider())
                                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
                // .exceptionHandling((exceptionHandling)-> exceptionHandling)
                return http.build();
        }

        @Bean
        public AuthenticationProvider authenticationProvider() {
                DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
                authenticationProvider.setUserDetailsService(customerUserDetailsService);
                authenticationProvider.setPasswordEncoder(passwordEncoder());
                return authenticationProvider;
        }

        @Bean
        public PasswordEncoder passwordEncoder() {
                return new BCryptPasswordEncoder();
        }

        @Bean
        public AuthenticationManager authenticationManager(
                        AuthenticationConfiguration authenticationConfiguration) throws Exception {
                return authenticationConfiguration.getAuthenticationManager();
        }

        @Bean
        CorsFilter corsFilter() {
                final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
                final CorsConfiguration config = new CorsConfiguration();
                config.setAllowCredentials(true);
                // Don't do this in production, use a proper list of allowed origins
                config.setAllowedOriginPatterns(Collections.singletonList("*"));
                config.setAllowedHeaders(Arrays.asList("Origin", "Content-Type", "Accept",
                                "Authorization"));
                config.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "OPTIONS",
                                "DELETE", "PATCH"));
                source.registerCorsConfiguration("/**", config);
                // some comment here
                return new CorsFilter(source);
        }
}
