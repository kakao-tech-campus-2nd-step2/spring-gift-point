package gift.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.PathItem;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.parameters.HeaderParameter;
import io.swagger.v3.oas.models.responses.ApiResponse;
import org.springdoc.core.customizers.OpenApiCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Set;

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

    @Bean
    public OpenApiCustomizer openApiCustomizer() {
        return openApi -> openApi.getPaths()
                .forEach(this::setBaseOperationResponse);
    }

    private void setBaseOperationResponse(String path, PathItem pathItem) {
        var excludePaths = Set.of("/api/members/login/kakao", "/api/members/login/kakao/callback", "/api/members/login", "/api/members/register");

        for (var operation : pathItem.readOperations()) {
            if (!excludePaths.contains(path)) {
                var header = new HeaderParameter()
                        .name("Authorization")
                        .required(Boolean.TRUE);
                operation.addParametersItem(header);

                var unauthorizedResponse = new ApiResponse().description("잘못된 인증정보");
                operation.getResponses().addApiResponse("401", unauthorizedResponse);
            }
        }
    }
}
