package gift.config;

import gift.util.jwt.JwtService;
import gift.util.resolver.CustomPageableHandlerMethodArgumentResolver;
import gift.util.resolver.LoginUserArgumentResolver;
import java.util.List;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    private final JwtService jwtService;

    public WebConfig(JwtService jwtService) {
        this.jwtService = jwtService;
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {

        resolvers.add(new CustomPageableHandlerMethodArgumentResolver());
        resolvers.add(new LoginUserArgumentResolver(jwtService));
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("*")  // 모든 출처 허용
                .allowedMethods("*")  // 모든 HTTP 메서드 허용
                .allowedHeaders("*")  // 모든 헤더 허용
                .exposedHeaders("Location")  // Location 헤더 노출
                .maxAge(3600);  // pre-flight 요청 캐시 시간
    }

}
