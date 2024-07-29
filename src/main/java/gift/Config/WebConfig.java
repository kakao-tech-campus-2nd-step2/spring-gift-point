package gift.Config;

import gift.Interceptor.ProductInterceptor;
import gift.Interceptor.WishInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    private final WishInterceptor wishInterceptor;
    private final ProductInterceptor productInterceptor;

    public WebConfig(WishInterceptor wishInterceptor, ProductInterceptor productInterceptor){
        this.wishInterceptor = wishInterceptor;
        this.productInterceptor = productInterceptor;
    }

    @Bean
    public RestTemplate restTemplate(){
        return new RestTemplate();
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(wishInterceptor).addPathPatterns("/api/v1/wishlist/**");
        registry.addInterceptor(productInterceptor).addPathPatterns("/api/products/**");
    }
}
