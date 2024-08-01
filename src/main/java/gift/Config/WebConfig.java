package gift.Config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    private final InterceptorOfToken interceptorOfToken;
    private final LoginUserArgumentResolver loginUserArgumentResolver;

    @Autowired
    public WebConfig(InterceptorOfToken interceptorOfToken, LoginUserArgumentResolver loginUserArgumentResolver) {
        this.interceptorOfToken = interceptorOfToken;
        this.loginUserArgumentResolver = loginUserArgumentResolver;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(interceptorOfToken).addPathPatterns("/**");
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(loginUserArgumentResolver);
    }
}
