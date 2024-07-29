package gift.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI openAPIConfig() {
        return new OpenAPI()
            .info(new Info()
                .title("spring-gift")
                .version("1.0")
                .description("KakaoTech campus step2 Kakao-gift Api"));
    }

}
