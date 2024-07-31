package gift.config;

import gift.resolver.LoginMemberResolver;
import gift.service.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@Configuration
@EnableWebMvc
public class WebMvcConfig implements WebMvcConfigurer {

    private final TokenService tokenService;

    @Autowired
    public WebMvcConfig(TokenService tokenService) {
        this.tokenService = tokenService;
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(new LoginMemberResolver(tokenService));
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("http://43.203.234.144:8080") // 허용할 출처 설정
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS") // 허용할 HTTP 메서드 설정
                .allowedHeaders("*") // 허용할 헤더 설정
                .allowCredentials(true); // 자격 증명 허용 설정
    }

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}
