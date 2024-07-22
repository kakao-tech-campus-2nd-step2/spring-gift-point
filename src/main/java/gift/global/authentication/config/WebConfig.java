package gift.global.authentication.config;

import gift.global.authentication.annotation.MemberIdResolver;
import gift.global.authentication.interceptor.TokenCheckInterceptor;
import java.util.List;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    private final TokenCheckInterceptor tokenCheckInterceptor;
    private final MemberIdResolver memberIdResolver;

    public WebConfig(TokenCheckInterceptor tokenCheckInterceptor, MemberIdResolver memberIdResolver) {
        this.tokenCheckInterceptor = tokenCheckInterceptor;
        this.memberIdResolver = memberIdResolver;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(tokenCheckInterceptor)
            .addPathPatterns("/api/**")
            .excludePathPatterns(
                "/api/members/register",
                "/api/members/login",
                "/api/members/reissue"
            );
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(memberIdResolver);
    }
}
