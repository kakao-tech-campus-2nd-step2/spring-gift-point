package gift.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Spring-Gift API")
                        .version("1.0")
                        .description("Spring-Gift API Documentation")
                        .contact(new Contact()
                                .name("이진솔")
                                .email("이진솔@naver.com")));
    }
}