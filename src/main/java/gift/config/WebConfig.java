package gift.config;

import gift.security.AuthenticateMemberArgumentResolver;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    private final AuthenticateMemberArgumentResolver authenticateMemberArgumentResolver;

    public WebConfig(AuthenticateMemberArgumentResolver authenticateMemberArgumentResolver){
        this.authenticateMemberArgumentResolver = authenticateMemberArgumentResolver;
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers){
        resolvers.add(authenticateMemberArgumentResolver);
    }
}
