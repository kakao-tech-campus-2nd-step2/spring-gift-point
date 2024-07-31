package gift.Config;

import gift.Interceptor.AdminInterceptor;
import gift.Interceptor.ConsumerInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    private final ConsumerInterceptor consumerInterceptor;
    private final AdminInterceptor adminInterceptor;

    public WebConfig(ConsumerInterceptor consumerInterceptor, AdminInterceptor adminInterceptor){
        this.consumerInterceptor = consumerInterceptor;
        this.adminInterceptor = adminInterceptor;
    }

    @Bean
    public RestTemplate restTemplate(){
        return new RestTemplate();
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(consumerInterceptor).addPathPatterns("/api/categories/**");
        registry.addInterceptor(consumerInterceptor).addPathPatterns("/api/products/**");
        registry.addInterceptor(consumerInterceptor).addPathPatterns("/api/wishes/**");
        registry.addInterceptor(consumerInterceptor).addPathPatterns("/api/orders/**");

    }
}
