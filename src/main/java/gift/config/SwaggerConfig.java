package gift.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("카카오 선물하기 API - 부산대_BE 강수민")
                        .version("1.0")
                        .description("이 API는 카카오 선물하기를 위해 설계되었습니다. " +
                                "주요 기능은 다음과 같습니다." +
                                "\n- 상품, 카테고리, 옵션 CRUD" +
                                "\n- 회원 등록 및 로그인" +
                                "\n- 상품 위시리스트 담기" +
                                "\n- 상품 주문하기")
                        .contact(new Contact().name("강수민").email("ramiregi@pusan.ac.kr")))
                .addServersItem(new Server().url("/").description("Default Server URL"));
    }
}

