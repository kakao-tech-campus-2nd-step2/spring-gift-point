package gift.Config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;

@Configuration
public class ApiConfig {
    @Bean
    public OpenAPI openAPI() {
        Info info = new Info()
                .title("충남대 백엔드 3조 김민지 API")
                .version("0.0.1");
        return new OpenAPI()
                .components(new Components())
                .info(info);
    }
}
