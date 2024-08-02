package gift.auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {


    private final Interceptor interceptor;

    @Autowired
    public WebConfig(Interceptor interceptor) {
        this.interceptor = interceptor;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(interceptor)
            .addPathPatterns("/**")
            .excludePathPatterns(
                "/api/login",
                "/api/login/*",
                "/api/user/register",
                "/api/social/token/*",
                "/api/social/code/**",
                "/swagger-ui/**",
                "/swagger-resources/**",
                "/v3/api-docs/**",

                "/api/category/detail"
            );
    }
}
