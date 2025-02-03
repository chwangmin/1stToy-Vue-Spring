package com.first.board.global.swagger;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import io.swagger.v3.oas.models.security.SecurityScheme.In;
import io.swagger.v3.oas.models.security.SecurityScheme.Type;

@OpenAPIDefinition(
        info = @Info(
                title = "Owing api 명세서",
                description = "### Owing API Reference for Developers\n\n" +
                        "eyJhbGciOiJIUzI1NiJ9.eyJtZW1iZXJJZCI6InN0cmluZyIsIm5hbWUiOiJzdHJpbmciLCJpYXQiOjE3Mzg1NjkyMTAsImV4cCI6MTc5ODU2OTIxMH0.O8fL3NiIJjX4AmtF79KIYVYtzzLmuKdhxlpvCFtE01g\n",
                version = "v1",
                contact = @Contact(
                        name = "LutesInfo",
                        email = "choi7907@lutes.co.kr",
                        url = ""
                )
        )
)
@Configuration
public class SwaggerConfig {

    private static final String SECURITY_SCHEME_NAME = "JWT";

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .addSecurityItem(new SecurityRequirement().addList(SECURITY_SCHEME_NAME))
                .components(new io.swagger.v3.oas.models.Components()
                        .addSecuritySchemes(SECURITY_SCHEME_NAME, createJwtSecurityScheme()));
    }

    private SecurityScheme createJwtSecurityScheme() {
        return new SecurityScheme()
                .name(SECURITY_SCHEME_NAME)
                .type(Type.HTTP)
                .scheme("bearer")
                .bearerFormat("JWT")
                .in(In.HEADER)
                .description("JWT Bearer Token을 사용한 인증. Bearer 키에 토큰을 입력하세요.");
    }
}

