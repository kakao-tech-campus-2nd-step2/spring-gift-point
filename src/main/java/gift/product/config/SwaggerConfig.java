package gift.product.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.Operation;
import io.swagger.v3.oas.models.media.StringSchema;
import io.swagger.v3.oas.models.parameters.HeaderParameter;
import io.swagger.v3.oas.models.parameters.Parameter;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import java.util.Set;
import org.springdoc.core.customizers.OpenApiCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(info = @Info(
    title = "카카오 선물하기 API",
    description = "카카오 선물하기 관련 기능 제공",
    version = "1.0.0"
))
public class SwaggerConfig {

    @Bean
    public OpenAPI api() {
        SecurityScheme apiKey = new SecurityScheme()
            .type(SecurityScheme.Type.APIKEY)
            .in(SecurityScheme.In.HEADER)
            .name("Authorization");

        SecurityRequirement securityRequirement = new SecurityRequirement()
            .addList("Bearer Token");

        return new OpenAPI()
            .components(new Components().addSecuritySchemes("Bearer Token", apiKey))
            .addSecurityItem(securityRequirement);
    }

    @Bean
    public OpenApiCustomizer customAuthParameter() {
        Set<String> targetPaths = Set.of(
            "/api/wishes",
            "/api/orders",
            "/api/members/login/kakao/unlink");

        return openApi -> openApi
            .getPaths()
            .forEach((path, pathItem) -> {
                boolean isTargetPath = false;
                for (String targetPath : targetPaths) {
                    if (path.startsWith(targetPath)) {
                        isTargetPath = true;
                        break;
                    }
                }

                if (isTargetPath) {
                    pathItem.readOperations().forEach(
                        this::addAuthParam
                    );
                }
            });
    }

    private void addAuthParam(Operation operation) {
        operation.addParametersItem(authHeader());
    }

    private Parameter authHeader() {
        return new HeaderParameter()
            .name("Authorization")
            .description("엑세스 토큰")
            .required(true)
            .schema(new StringSchema());
    }
}