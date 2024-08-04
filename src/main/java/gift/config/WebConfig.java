package gift.config;

import gift.resolver.LoginMemberArgumentResolver;
import java.util.List;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    private final LoginMemberArgumentResolver loginMemberArgumentResolver;

    public WebConfig(LoginMemberArgumentResolver loginMemberArgumentResolver) {
        this.loginMemberArgumentResolver = loginMemberArgumentResolver;
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(loginMemberArgumentResolver);
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/api/**")
            .allowedOrigins("*")
            .allowedMethods("GET", "OPTIONS")
            .allowedHeaders("*")
            .maxAge(1800);

        registry.addMapping("/api/members/**")
            .allowedOrigins("*")
            .allowedMethods("POST", "OPTIONS")
            .allowedHeaders("*")
            .maxAge(1800);

        registry.addMapping("/api/members/points")
            .allowedOrigins("*")
            .allowedMethods("PUT", "OPTIONS")
            .allowedHeaders("Authorization", "Content-type")
            .maxAge(1800);

        registry.addMapping("/api/wishes/**")
            .allowedOrigins("*")
            .allowedMethods("GET", "POST", "DELETE", "OPTIONS")
            .allowedHeaders("Authorization", "Content-Type")
            .allowCredentials(true)
            .maxAge(1800);

        registry.addMapping("/api/order")
            .allowedOrigins("*")
            .allowedMethods("POST", "OPTIONS")
            .allowedHeaders("Authorization", "Content-Type")
            .allowCredentials(true)
            .maxAge(1800);
    }
}
