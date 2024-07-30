package gift.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@OpenAPIDefinition(
    info = @Info(
        title = "Spring gift API",
        description = "Spring gift 프로젝트 API 명세서입니다.",
        version = "v0.0.1"
    )
)
@Configuration
public class SwaggerConfig {

    public static final String HEADER_TYPE = "Bearer";

    @Bean
    public OpenAPI openAPI() {
        String jwt = "JWT";
        SecurityRequirement securityRequirement = new SecurityRequirement().addList(jwt);
        Components components = new Components()
            .addSecuritySchemes(jwt, new SecurityScheme()
                .name(jwt)
                .type(SecurityScheme.Type.HTTP)
                .scheme(HEADER_TYPE)
                .bearerFormat(jwt));

        return new OpenAPI()
            .addSecurityItem(securityRequirement)
            .components(components);
    }
}
