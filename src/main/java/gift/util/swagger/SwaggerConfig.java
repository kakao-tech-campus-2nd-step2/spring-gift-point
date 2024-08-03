package gift.util.swagger;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import java.util.Arrays;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI openAPI() {
        SecurityScheme securityScheme = new SecurityScheme()
            .type(SecurityScheme.Type.HTTP)
            .scheme("bearer").bearerFormat("JWT")
            .in(SecurityScheme.In.HEADER)
            .name("Authorization");
        SecurityRequirement securityRequirement = new SecurityRequirement().addList("bearerAuth");

        return new OpenAPI()
            .info(apiInfo())
            .components(new Components().addSecuritySchemes("bearerAuth", securityScheme))
            .security(Arrays.asList(securityRequirement));
    }

    public Info apiInfo() {
        return new Info()
            .title("kakao tech camp step2")
            .description("kakao tech camp step2 선물하기 기능 구현<br>"
                + "<li>로그인으로 bearer토큰을 얻어서 사용</li>"
                + "<li>카카오 로그인으로 토큰을 얻어서, 카카오 토큰으로 소셜 로그인(카카오)을 해야 bearer토큰을 얻어서 사용해야 함</li>"
                + "<li>카카오로 로그인해야 상품구매시 카카오톡으로 메세지 보냄</li>")
            .version("1.0");
    }
}
