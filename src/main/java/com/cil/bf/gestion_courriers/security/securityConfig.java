package com.cil.bf.gestion_courriers.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;

@SuppressWarnings("deprecation")
@Configuration
@EnableWebSecurity
public class securityConfig {

        // @Autowired
        // CustomerUserDetailsService customerUserDetailsService;

        // @Autowired
        // JwtFilter jwtFilter;

        // protected void configure(AuthenticationManagerBuilder auth) throws Exception
        // {
        // auth.userDetailsService(customerUserDetailsService);
        // // .passwordEncoder(passwordEncoder());
        // }

        @Bean
        PasswordEncoder passwordEncoder() {
                return NoOpPasswordEncoder.getInstance();
        }

        @Bean
        // (name = BeanIds.AUTHENTICATION_MANAGER)
        public AuthenticationManager authenticationManager(
                        AuthenticationConfiguration authenticationConfiguration) throws Exception {
                return authenticationConfiguration.getAuthenticationManager();
        }

        @Bean
        public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
                http
                                // .addFilterBefore(corsFilter(), SessionManagementFilter.class)
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
                                                                "/**")
                                                .permitAll()
                                                .anyRequest().authenticated())

                                .sessionManagement((session) -> session
                                                .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                // .exceptionHandling((exceptionHandling)-> exceptionHandling)
                ;
                // http.addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);
                return http.build();
        }

        // @Bean
        // CorsFilter corsFilter() {
        // final UrlBasedCorsConfigurationSource source = new
        // UrlBasedCorsConfigurationSource();
        // final CorsConfiguration config = new CorsConfiguration();
        // config.setAllowCredentials(true);
        // // Don't do this in production, use a proper list of allowed origins
        // config.setAllowedOriginPatterns(Collections.singletonList("*"));
        // config.setAllowedHeaders(Arrays.asList("Origin", "Content-Type", "Accept",
        // "Authorization"));
        // config.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "OPTIONS",
        // "DELETE", "PATCH"));
        // source.registerCorsConfiguration("/**", config);
        // // some comment here
        // return new CorsFilter(source);
        // }
}
