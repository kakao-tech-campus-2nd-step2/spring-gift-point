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
            .allowedOriginPatterns("*")
            .allowedMethods("GET", "POST", "PUT", "DELETE")
            .allowedHeaders("Content-Type", "Authorization")
            .exposedHeaders("Authorization", "Location")
            .maxAge(1800)
            .allowCredentials(true);

        registry.addMapping("/api/categories/**")
            .allowedOriginPatterns("*")
            .allowedMethods("GET", "POST", "PUT", "DELETE")
            .allowedHeaders("Content-Type", "Authorization")
            .exposedHeaders("Authorization", "Location")
            .maxAge(1800)
            .allowCredentials(true);

        registry.addMapping("/api/orders/**")
            .allowedOriginPatterns("*")
            .allowedMethods("POST")
            .allowedHeaders("Content-Type", "Authorization")
            .exposedHeaders("Authorization")
            .maxAge(1800)
            .allowCredentials(true);

        registry.addMapping("/api/wishes/**")
            .allowedOriginPatterns("*")
            .allowedMethods("GET", "POST", "DELETE")
            .allowedHeaders("Content-Type", "Authorization")
            .exposedHeaders("Authorization")
            .maxAge(1800)
            .allowCredentials(true);

        registry.addMapping("/api/members/**")
            .allowedOriginPatterns("*")
            .allowedMethods("POST")
            .allowedHeaders("Content-Type")
            .exposedHeaders("Authorization")
            .maxAge(1800)
            .allowCredentials(true);

        registry.addMapping("/api/products/{productId}/options/**")
            .allowedOriginPatterns("*")
            .allowedMethods("GET", "POST", "PUT", "DELETE")
            .allowedHeaders("Content-Type", "Authorization")
            .exposedHeaders("Authorization", "Location")
            .maxAge(1800)
            .allowCredentials(true);
    }
}