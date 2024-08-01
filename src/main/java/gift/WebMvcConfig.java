package gift;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {
    private final TokenValidationInterceptor tokenValidationInterceptor;

    public WebMvcConfig(TokenValidationInterceptor tokenValidationInterceptor) {
        this.tokenValidationInterceptor = tokenValidationInterceptor;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(tokenValidationInterceptor)
                .addPathPatterns("/api/products")
                .addPathPatterns("/api/products/**")
                .addPathPatterns("/api/wishlist")
                .addPathPatterns("/api/wishlist/**");
    }
}