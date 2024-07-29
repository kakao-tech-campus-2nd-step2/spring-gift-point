package gift.global.config;


import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    private static final String BEARER_KEY = "bearer-key";

    @Bean
    public OpenAPI openAPI() {
        var securityRequirement = new SecurityRequirement();
        securityRequirement.addList(BEARER_KEY);

        return new OpenAPI()
            .components(components())
            .info(info())
            .addSecurityItem(securityRequirement);
    }

    private Components components() {
        return new Components()
            .addSecuritySchemes(BEARER_KEY, securityScheme());
    }

    private SecurityScheme securityScheme() {
        return new SecurityScheme()
            .type(SecurityScheme.Type.HTTP)
            .scheme("bearer")
            .bearerFormat("JWT")
            .description("JWT 인증을 위한 Bearer 토큰을 입력하세요.(예 : Bearer {토큰}에서 {토큰}만 입력)");
    }

    private Info info() {
        return new Info()
            .title("KakaoCloneProject")
            .description("KakaoCloneProject API 명세서")
            .version("1.0.0");
    }
}
