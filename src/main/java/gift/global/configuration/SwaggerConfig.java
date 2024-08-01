package gift.global.configuration;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.security.SecurityScheme.In;
import io.swagger.v3.oas.models.security.SecurityScheme.Type;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;

// frontend 코드를 작성하다가 api 관리가 필요하다고 생각해서 만들었습니다.
@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI()
            .components(new Components().addSecuritySchemes(HttpHeaders.AUTHORIZATION,
                new SecurityScheme().type(Type.APIKEY).in(In.HEADER)
                    .name(HttpHeaders.AUTHORIZATION)))
            .info(apiInfo());
    }

    private Info apiInfo() {
        return new Info()
            .title("gift api Document")
            .description("선물하기 api 모음입니다")
            .version("1.0.0");
    }
}
