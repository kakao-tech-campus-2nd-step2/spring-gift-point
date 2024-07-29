package gift.config;

import gift.annotation.LoginUserArgumentResolver;
import gift.jwt.JWTService;
import gift.jwt.JwtInterceptor;
import gift.user.UserService;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@Configuration
public class Config implements WebMvcConfigurer {

    private final JwtInterceptor jwtInterceptor;
    private final JWTService jwtService;
    private final UserService userService;

    public Config(JwtInterceptor jwtInterceptor, JWTService jwtService, UserService userService) {
        this.jwtInterceptor = jwtInterceptor;
        this.jwtService = jwtService;
        this.userService = userService;

    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
//        registry.addInterceptor(jwtInterceptor)
//                .addPathPatterns("/*")
//                .excludePathPatterns("/members/**", "/manager/products", "/manager/products/**", "/manager/products/**/**", "**.html", "/login");
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(new LoginUserArgumentResolver(userService, jwtService));
    }
}
