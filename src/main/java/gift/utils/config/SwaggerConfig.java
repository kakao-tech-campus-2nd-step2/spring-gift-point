package gift.utils.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.servers.Server;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
            .components(new Components()
                .addSecuritySchemes("bearer-jwt", new SecurityScheme()
                    .type(SecurityScheme.Type.HTTP)
                    .scheme("bearer")
                    .bearerFormat("JWT")
                )
            )
            .info(new Info()
                .title("카카오 선물하기 API - 부산대_BE 박정우")
                .version("1.0")
                .description("이 API는 카카오 선물하기를 위해 설계되었습니다. 주요기능으로는 내게 선물하기, 상품, 옵션, 카테고리 ,유저의 crud가 있습니다")
                .contact(new Contact().name("박정우").email("rmrmrmrm12@pusan.ac.kr")))
                .addSecurityItem(new SecurityRequirement().addList("bearer-jwt"))
            .addServersItem(new Server().url("/").description("Default Server URL"));
    }
}
