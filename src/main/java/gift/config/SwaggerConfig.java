package gift.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class SwaggerConfig {

    @Value("${server.local-url}")
    private String localUrl;
    @Value("${server.deploy-url}")
    private String deployUrl;

    @Bean
    public OpenAPI openAPI() {
        String jwt = "accessToken";
        SecurityRequirement securityRequirement = new SecurityRequirement().addList(jwt);
        Components components = new Components().addSecuritySchemes(jwt, new SecurityScheme()
                .name(jwt)
                .type(SecurityScheme.Type.HTTP)
                .scheme("bearer")
                .bearerFormat("JWT")
        );

        Server local = new Server();
        Server deploy = new Server();

        local.setUrl(localUrl);
        deploy.setUrl(deployUrl);

        return new OpenAPI()
                .components(components)
                .info(apiInfo())
                .servers(List.of(local, deploy))
                .addSecurityItem(securityRequirement);
    }

    private Info apiInfo() {
        return new Info()
                .title("KakaoTechCampus Spring-Gift API Docs")
                .description("Step2 클론코딩(카카오톡 선물하기) minju26의 API 명세입니다.")
                .version("1.0.0");
    }
}