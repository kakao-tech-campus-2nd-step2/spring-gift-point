package gift.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI openAPI() {
        var info = new Info()
                .version("v1.0")
                .title("카카오테크캠퍼스-선물하기")
                .description("프론트엔드와 협업을 위한 API 문서");
        return new OpenAPI().info(info);
    }
}
