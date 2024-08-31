package com.cil.bf.gestion_courriers.swagger;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;

import java.util.Arrays;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.CollectionUtils;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

@OpenAPIDefinition
@Configuration
public class SpringdocConfig {

        final String securitySchemeName = "bearerAuth";

        @Bean
        public CorsFilter corsFilter() {
                UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
                CorsConfiguration config = new CorsConfiguration();
                config.addAllowedOrigin("*");
                config.addAllowedHeader("*");
                config.addAllowedMethod("*");
                config.setAllowCredentials(false);
                config.setMaxAge(1800L);
                config.setExposedHeaders(
                                Arrays.asList("Authorization,Link,X-Total-Count,X-GESCO-alert,X-GESCO-error,X-GESCO-params"));
                if (!CollectionUtils.isEmpty(config.getAllowedOrigins())) {
                        source.registerCorsConfiguration("/api/**", config);
                        source.registerCorsConfiguration("/v3/api-docs", config);
                        source.registerCorsConfiguration("/swagger-resources", config);
                        source.registerCorsConfiguration("/swagger-ui/**", config);

                }
                return new CorsFilter(source);
        }

        @Bean
        public OpenAPI baseOpenAPI() {
                Server localServer = new Server()
                                .url("http://localhost:8081")
                                .description("Localhost Server URL");
                return new OpenAPI().info(new Info()
                                .title("Gestion Courrier")
                                .version("1.0.0")
                                .description("Une documentation de Gestion du Courrier"))
                                .addSecurityItem(new SecurityRequirement()
                                                .addList(securitySchemeName))
                                .components(new Components()
                                                .addSecuritySchemes(securitySchemeName, new SecurityScheme()
                                                                .name(securitySchemeName)
                                                                .type(SecurityScheme.Type.HTTP)
                                                                .scheme("bearer")
                                                                .bearerFormat("JWT"))

                                )
                                .addServersItem(localServer);

        }
}