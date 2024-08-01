package gift.global.auth.config;

import gift.global.auth.resolver.LoginMemberArgumentResolver;
import gift.user.service.UserService;
import gift.utility.JwtUtil;
import java.util.List;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    private final UserService userService;

    public WebConfig(UserService userService) {
        this.userService = userService;
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(new LoginMemberArgumentResolver(userService));
    }

    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**")  // 모든 경로에 대해
                    .allowedOrigins("*")  // 모든 오리진에 대해
                    .allowedMethods("*")  // 모든 HTTP 메소드에 대해
                    .allowedHeaders("*");
            }
        };
    }
}
