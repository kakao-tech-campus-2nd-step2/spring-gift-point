package gift.advice;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {
    private final LoggedInUserArgumentResolver loggedInUserArgumentResolver;
    private final GatewayTokenArgumentResolver gatewayTokenArgumentResolver;

    @Autowired
    public WebMvcConfig(
            LoggedInUserArgumentResolver loggedInUserArgumentResolver,
            GatewayTokenArgumentResolver gatewayTokenArgumentResolver
    ) {
        this.loggedInUserArgumentResolver = loggedInUserArgumentResolver;
        this.gatewayTokenArgumentResolver = gatewayTokenArgumentResolver;
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(loggedInUserArgumentResolver);
        resolvers.add(gatewayTokenArgumentResolver);
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new GatewayTokenInterceptor());
    }
}
