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
    public OpenAPI springShopOpenAPI() {
        String description = "이 문서는 Gift API의 명세서입니다.<br>"
            + "이 문서를 통해 API의 사용법과 엔드포인트에 대한 정보를 확인할 수 있습니다.<br><br>"
            + "API 사용 시 주의사항:<br>"
            + "위시,주문 관련 요청은 인증이 필요합니다.<br>"
            + " > 사용자 로그인 요청을 통해 얻은 token을 오른쪽 Authorize에 넣으면 요청 헤더에 인증 정보가 자동으로 들어갑니다.<br>"
            + "카카오 인증 리다이렉트 엔드포인트는 Swagger UI에서 직접 호출할 수 없습니다. 브라우저를 통해 직접 접근해야 합니다.<br>"
            + "<br>[작성자: 이지호 (깃허브 GitJIHO)]";

        Info info = new Info()
            .title("Gift API")
            .description(description)
            .version("v0.0.1");

        String jwtSchemeName = "jwtAuth";

        SecurityRequirement securityRequirement = new SecurityRequirement().addList(jwtSchemeName);

        Components components = new Components()
            .addSecuritySchemes(jwtSchemeName, new SecurityScheme()
                .name(jwtSchemeName)
                .type(SecurityScheme.Type.HTTP)
                .scheme("bearer")
                .bearerFormat("JWT"));

        return new OpenAPI()
            .components(new Components())
            .info(info)
            .addSecurityItem(securityRequirement)
            .components(components);
    }
}

