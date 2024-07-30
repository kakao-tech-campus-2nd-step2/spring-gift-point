package gift.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {
    @Bean
    public OpenAPI openAPI() {
        SecurityRequirement securityRequirement = new SecurityRequirement();
        securityRequirement.addList("bearerAuth");

        return new OpenAPI()
            .components(new Components())
            .info(info())
            .addSecurityItem(securityRequirement)
            .components(components());
    }

    private Info info() {
        return new Info()
            .title("카카오 선물하기 클론 프로젝트")
            .description("카카오 선물하기 클론 프로젝트의 API 명세서입니다.")
            .version("v1.0.0");
    }

    private SecurityScheme securityScheme() {
        return new SecurityScheme()
            .type(SecurityScheme.Type.HTTP)
            .in(SecurityScheme.In.HEADER)
            .name("Authorization")
            .scheme("bearer")
            .bearerFormat("JWT")
            .description("Bearer {JwtToken}");
    }

    private Components components() {
        return new Components()
            .addSecuritySchemes("bearerAuth", securityScheme());
    }

}
