package gift.config;

import gift.domain.JwtInterceptor;
import gift.domain.JwtToken;
import gift.domain.LoginUserArgumentResolver;
import java.util.List;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    private JwtInterceptor jwtInterceptor;

    private JwtToken jwtToken;

    public WebConfig(JwtInterceptor jwtInterceptor, JwtToken jwtToken) {
        this.jwtInterceptor = jwtInterceptor;
        this.jwtToken = jwtToken;
    }


    @Override
    public void addInterceptors(InterceptorRegistry registry){
        registry.addInterceptor(jwtInterceptor).addPathPatterns("/api/wishes/**");
        registry.addInterceptor(jwtInterceptor).addPathPatterns("/api/orders/**");
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
        argumentResolvers.add(new LoginUserArgumentResolver(jwtToken));
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
            .allowedOriginPatterns("*")
            .allowedMethods("*")
            .allowedOrigins("*")
            .allowCredentials(true);
    }

}
