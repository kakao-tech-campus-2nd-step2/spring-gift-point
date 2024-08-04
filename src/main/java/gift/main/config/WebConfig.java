package gift.main.config;

import gift.main.interceptor.AuthInterceptor;
import gift.main.resolver.SessionUserArgumentResolver;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@Configuration
@EnableWebMvc
public class WebConfig implements WebMvcConfigurer {

    private final AuthInterceptor authLoginInterceptor;

    public WebConfig(AuthInterceptor authLoginInterceptor) {
        this.authLoginInterceptor = authLoginInterceptor;
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(new SessionUserArgumentResolver());
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // 인터셉터를 등록하는 메서드/members/register
        // CustomInterceptor를 등록하고, 모든 URL에 대해 인터셉터를 적용하도록 설정
        registry.addInterceptor(authLoginInterceptor)
                .addPathPatterns("/api/orders/**", "/api/orders", "/ap/iwishes/**", "/api/wishes","/api/point");

    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("http://localhost:3000") // 3000 포트에서 오는 요청 허용
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS") // 허용할 HTTP 메서드
                .allowCredentials(true) // 쿠키를 사용할 경우
                .allowedHeaders("*"); // 허용할 헤더
    }

}
