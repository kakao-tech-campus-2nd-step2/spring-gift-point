package gift.common.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
            .info(new Info().title("선물하기 API")
                .version("1.0")
                .description("선물하기 API 명세서입니다."));
    }

    @Bean
    public GroupedOpenApi productApi() {
        return GroupedOpenApi.builder()
            .group("product")
            .pathsToMatch("/api/products/**", "/api/products/{productId}", "/api/products/paged")
            .build();
    }

    @Bean
    public GroupedOpenApi memberApi() {
        return GroupedOpenApi.builder()
            .group("member")
            .pathsToMatch("/api/members/**")
            .build();
    }

    @Bean
    public GroupedOpenApi categoryApi() {
        return GroupedOpenApi.builder()
            .group("category")
            .pathsToMatch("/api/categories/**")
            .build();
    }

    @Bean
    public GroupedOpenApi wishApi() {
        return GroupedOpenApi.builder()
            .group("wish")
            .pathsToMatch("/api/wishes/**")
            .build();
    }

    @Bean
    public GroupedOpenApi kakaoApi() {
        return GroupedOpenApi.builder()
            .group("kakao")
            .pathsToMatch("/api/kakao/**")
            .build();
    }

    @Bean
    public GroupedOpenApi OrderApi() {
        return GroupedOpenApi.builder()
            .group("order")
            .pathsToMatch("/api/orders/**")
            .build();
    }

}
