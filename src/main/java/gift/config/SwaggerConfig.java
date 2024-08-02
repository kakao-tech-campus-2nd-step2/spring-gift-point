package gift.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Collections;

@Configuration
public class SwaggerConfig {
    @Bean
    public OpenAPI openAPI() {
        SecurityScheme securityScheme = new SecurityScheme()
                .type(SecurityScheme.Type.HTTP)
                .scheme("bearer")
                .bearerFormat("JWT")
                .in(SecurityScheme.In.HEADER)
                .name("Authorization");
        SecurityRequirement securityRequirement = new SecurityRequirement().addList("bearerAuth");
        return new OpenAPI()
                .info(apiInfo())
                .components(new Components().addSecuritySchemes("bearerAuth", securityScheme))
                .security(Collections.singletonList(securityRequirement));
    }

    public Info apiInfo() {
        return new Info()
                .title("kakao tech camp step2")
                .description("kakao tech camp step2")
                .version("1.0");
    }
}