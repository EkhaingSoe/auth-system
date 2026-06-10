package com.example.auth_system.common.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class SwaggerConfig {
    
    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(apiInfo())
                .servers(servers())
                .components(authComponents())
                .addSecurityItem(new SecurityRequirement().addList("Bearer Authentication"));
    }
    
    private Info apiInfo() {
        return new Info()
                .title("Auth System API Documentation")
                .description("Complete Authentication System with JWT, OTP, and Password Reset")
                .version("1.0.0")
                .contact(new Contact()
                        .name("Auth System Team")
                        .email("support@authsystem.com")
                        .url("https://github.com/auth-system"))
                .license(new License()
                        .name("Apache 2.0")
                        .url("http://springdoc.org"));
    }
    
    private List<Server> servers() {
        return List.of(
                new Server()
                        .url("http://localhost:8080")
                        .description("Development Server"),
                new Server()
                        .url("https://staging-api.yourdomain.com")
                        .description("Staging Server"),
                new Server()
                        .url("https://api.yourdomain.com")
                        .description("Production Server")
        );
    }
    
    private Components authComponents() {
        return new Components()
                .addSecuritySchemes("Bearer Authentication", new SecurityScheme()
                        .name("Bearer Authentication")
                        .type(SecurityScheme.Type.HTTP)
                        .scheme("bearer")
                        .bearerFormat("JWT")
                        .description("Enter JWT token. Example: 'eyJhbGciOiJIUzI1NiIs...'"));
    }
}