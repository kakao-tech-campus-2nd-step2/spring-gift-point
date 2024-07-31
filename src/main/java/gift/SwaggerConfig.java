package gift;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

  @Bean
  public GroupedOpenApi jwtApi() {
    return GroupedOpenApi.builder()
      .group("api") // 그룹설정
      .pathsToMatch("/**")
      .build();
  }

  @Bean
  public OpenAPI customOpenAPI() {
    SecurityRequirement securityRequirement = new SecurityRequirement().addList("Authorization");
    return new OpenAPI()
      .addSecurityItem(securityRequirement)
      .components(new Components()
        .addSecuritySchemes("Authorization",
          new SecurityScheme().type(SecurityScheme.Type.HTTP).scheme("bearer").bearerFormat("JWT")))
      .info(new Info().title("Kakao 선물하기 Swagger")
        .description("API 예시입니다")
        .version("0.0.1"));

  }
}