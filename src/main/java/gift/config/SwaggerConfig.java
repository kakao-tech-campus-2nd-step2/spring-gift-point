package gift.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
            .info(new Info()
                .title("카카오톡 선물하기 API")
                .version("1.0")
                .description("카카오톡 선물하기 API 명세서 - 부산대 BE 김동현")
                .contact(new Contact().name("김동현").email("gimdonghyun5434@gmail.com")))
            .addServersItem(new Server().url("/").description("Default Server URL"));
    }
}