package gift.common.config;

import gift.common.auth.LoginUserArgumentResolver;
import gift.common.auth.TokenInterceptor;
import java.util.List;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    private final TokenInterceptor tokenInterceptor;
    private final LoginUserArgumentResolver loginUserArgumentResolver;

    public WebConfig(TokenInterceptor tokenInterceptor,
        LoginUserArgumentResolver loginUserArgumentResolver) {
        this.tokenInterceptor = tokenInterceptor;
        this.loginUserArgumentResolver = loginUserArgumentResolver;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(tokenInterceptor)
            .addPathPatterns("/api/wishes/**", "/api/orders/**", "/api/admin/**", "/api/members/point");
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(loginUserArgumentResolver);
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
            .allowedOriginPatterns("*")
            .allowedMethods("GET", "POST", "PUT", "DELETE", "PATCH")
            .allowedHeaders("Authorization", "Content-Type")
            .allowCredentials(true)
            .exposedHeaders("Authorization")
            .maxAge(3600);
    }
}
