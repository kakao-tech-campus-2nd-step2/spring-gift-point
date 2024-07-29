package gift.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI customOpenAPI(){
        String securityJwtName = "JWT";
        return new OpenAPI()
            .addSecurityItem(new SecurityRequirement().addList(securityJwtName))
            .components(new Components()
                .addSecuritySchemes(securityJwtName, new SecurityScheme()
                    .type(SecurityScheme.Type.HTTP)
                    .scheme("Bearer")
                    .bearerFormat(securityJwtName)))
            .info(new Info()
                .title("spring-gift API")
                .version("1.0")
                .description("spring-gift API documentation")
                .contact(new Contact().name("박혜연").email("박혜연@kakao.com")));
    }
}
