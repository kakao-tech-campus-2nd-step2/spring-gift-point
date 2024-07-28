package gift.global.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.media.IntegerSchema;
import io.swagger.v3.oas.models.media.StringSchema;
import io.swagger.v3.oas.models.parameters.Parameter;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springdoc.core.customizers.OperationCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.Pageable;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI openAPI() {
        String jwt = "JWT";
        SecurityRequirement securityRequirement = new SecurityRequirement().addList(jwt);
        Components components = new Components().addSecuritySchemes(jwt, new SecurityScheme()
            .name(jwt)
            .type(SecurityScheme.Type.HTTP)
            .scheme("bearer")
            .bearerFormat("JWT")
        );
        return new OpenAPI()
            .components(new Components())
            .info(apiInfo())
            .addSecurityItem(securityRequirement)
            .components(components);
    }

    private Info apiInfo() {
        return new Info()
            .title("Gift API")
            .description("Kakao Tech Campus BE")
            .version("1.0.0");
    }

    @Bean
    public OperationCustomizer customizeOperation() {
        return (operation, handlerMethod) -> {
            var parameters = operation.getParameters();
            if (parameters != null) {
                parameters.removeIf(p -> "pageable".equals(p.getName()));
            }

            for (var param : handlerMethod.getMethodParameters()) {
                if (param.getParameterType().equals(Pageable.class)) {
                    operation.addParametersItem(new Parameter()
                        .name("page")
                        .description("Page number (0..N)")
                        .in("query")
                        .required(false)
                        .schema(new IntegerSchema()._default(0)));
                    operation.addParametersItem(new Parameter()
                        .name("size")
                        .description("Page size (1 to 100), Default: 20")
                        .in("query")
                        .required(false)
                        .schema(new IntegerSchema()._default(20)));
                    operation.addParametersItem(new Parameter()
                        .name("sort")
                        .description(
                            "Sort criteria in the format: property(,asc|desc), Default: modifiedDate,DESC")
                        .in("query")
                        .required(false)
                        .schema(new StringSchema()._default("modifiedDate,DESC")));
                }
            }
            return operation;
        };
    }
}
