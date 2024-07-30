package gift.global.config;

import gift.global.security.AuthenticationPrincipalArgumentResolver;
import gift.global.security.TokenManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {
    private final TokenManager tokenManager;

    public WebMvcConfig(TokenManager tokenManager) {
        this.tokenManager = tokenManager;
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(authenticationPrincipalArgumentResolver());
    }

    @Bean
    public HandlerMethodArgumentResolver authenticationPrincipalArgumentResolver() {
        return new AuthenticationPrincipalArgumentResolver(tokenManager);
    }
}
