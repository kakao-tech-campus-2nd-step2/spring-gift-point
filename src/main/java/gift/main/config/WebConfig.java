package gift.main.config;

import gift.main.interceptor.AuthInterceptor;
import gift.main.resolver.SessionUserArgumentResolver;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
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
                .excludePathPatterns("/members/login", "/members/join", "/kakao", "/", "/kakao/**")
                .addPathPatterns("/**");

    }

}
