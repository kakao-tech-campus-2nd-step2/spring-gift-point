package gift.config;

import gift.resolver.LoginMemberArgumentResolver;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    private final LoginMemberArgumentResolver loginMemberArgumentResolver;

    public WebConfig(LoginMemberArgumentResolver loginMemberArgumentResolver) {
        this.loginMemberArgumentResolver = loginMemberArgumentResolver;
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
        argumentResolvers.add(loginMemberArgumentResolver);
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/api/products/**")
            .allowedOrigins("*")
            .allowedMethods("GET", "POST", "PUT", "DELETE")
            .allowedHeaders("Content-Type", "Authorization")
            .exposedHeaders("Authorization", "Location")
            .maxAge(1800);

        registry.addMapping("/api/categories/**")
            .allowedOrigins("*")
            .allowedMethods("GET", "POST", "PUT", "DELETE")
            .allowedHeaders("Content-Type", "Authorization")
            .exposedHeaders("Authorization", "Location")
            .maxAge(1800);

        registry.addMapping("/api/orders/**")
            .allowedOrigins("*")
            .allowedMethods("POST")
            .allowedHeaders("Content-Type", "Authorization")
            .exposedHeaders("Authorization")
            .maxAge(1800);

        registry.addMapping("/api/wishes/**")
            .allowedOrigins("*")
            .allowedMethods("GET", "POST", "DELETE")
            .allowedHeaders("Content-Type", "Authorization")
            .exposedHeaders("Authorization")
            .maxAge(1800);

        registry.addMapping("/api/members/**")
            .allowedOrigins("*")
            .allowedMethods("POST")
            .allowedHeaders("Content-Type")
            .exposedHeaders("Authorization")
            .maxAge(1800);

        registry.addMapping("/api/products/{productId}/options/**")
            .allowedOrigins("*")
            .allowedMethods("GET", "POST", "PUT", "DELETE")
            .allowedHeaders("Content-Type", "Authorization")
            .exposedHeaders("Authorization", "Location")
            .maxAge(1800);
    }
}