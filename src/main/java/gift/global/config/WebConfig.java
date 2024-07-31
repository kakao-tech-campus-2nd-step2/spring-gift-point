package gift.global.config;

import gift.global.Interceptor.JwtInterceptor;
import gift.global.resolver.JwtAuthorizationArgumentResolver;
import java.util.List;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    private final JwtAuthorizationArgumentResolver jwtAuthorizationArgumentResolver;
    private final JwtInterceptor jwtInterceptor;

    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**")
                    .allowedOriginPatterns("*")
                    .allowedMethods("GET", "POST", "PUT", "DELETE", "HEAD", "OPTIONS")
                    .allowedHeaders("*")
                    .allowCredentials(true);
            }
        };
    }

    public WebConfig(
        JwtAuthorizationArgumentResolver jwtAuthorizationArgumentResolver,
        JwtInterceptor jwtInterceptor
    ) {
        this.jwtAuthorizationArgumentResolver = jwtAuthorizationArgumentResolver;
        this.jwtInterceptor = jwtInterceptor;
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(jwtAuthorizationArgumentResolver);
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(jwtInterceptor)
            .addPathPatterns("/members/wishes/**") // Controller
            .addPathPatterns(("/api/wishes/**")) // RestController
            .addPathPatterns("/api/orders/**"); // RestController
    }
}
