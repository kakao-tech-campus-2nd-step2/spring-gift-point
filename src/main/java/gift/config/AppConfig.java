package gift.config;

import gift.exception.RestTemplateResponseErrorHandler;
import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class AppConfig {

    @Bean
    public RestTemplate restTemplate(RestTemplateBuilder builder, RestTemplateResponseErrorHandler errorHandler) {
        return builder
            .errorHandler(errorHandler)
            .build();
    }

    @Bean
    public Dotenv dotenv() {
        return Dotenv.load();
    }
}

