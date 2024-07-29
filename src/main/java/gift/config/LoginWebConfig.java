package gift.config;

import gift.auth.AuthApiInterceptor;
import gift.auth.AuthMvcInterceptor;
import gift.auth.JwtTokenProvider;
import gift.resolver.LoginMemberArgumentResolver;
import java.util.List;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class LoginWebConfig implements WebMvcConfigurer {

    private final JwtTokenProvider jwtTokenProvider;

    public LoginWebConfig(JwtTokenProvider jwtTokenProvider) {
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new AuthApiInterceptor(jwtTokenProvider))
            .order(1)
            .addPathPatterns("/api/**")
            .excludePathPatterns("/api/login", "/api/join",
                "/view/**");
        registry.addInterceptor(new AuthMvcInterceptor(jwtTokenProvider))
            .order(2)
            .addPathPatterns("/view/**")
            .excludePathPatterns("/api/**","/view/products", "/view/join",
                "/view/login");

    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(new LoginMemberArgumentResolver());
    }

}
