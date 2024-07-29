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

    private static final String BEARER_PREFIX = "Bearer";

    @Bean
    public OpenAPI openAPI() {
        String security = "JWT";

        SecurityRequirement securityRequirement = new SecurityRequirement().addList(security);

        Components components = new Components()
            .addSecuritySchemes(security, new SecurityScheme()
                .name(security)
                .type(SecurityScheme.Type.HTTP)
                .scheme(BEARER_PREFIX)
                .bearerFormat(security));

        return new OpenAPI()
            .addSecurityItem(securityRequirement)
            .components(components)
            .info(apiInfo());
    }

    private Info apiInfo() {
        return new Info()
            .title("Kakao-tech-campus-Spring-gift API")
            .description("Kakao gift clone coding")
            .version("1.0.0");
    }

}
