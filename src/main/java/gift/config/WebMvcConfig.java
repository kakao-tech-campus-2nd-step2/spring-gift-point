package gift.config;

import gift.security.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.web.filter.HiddenHttpMethodFilter;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {
    private final JwtProvider jwtProvider;

    public WebMvcConfig(JwtProvider jwtProvider) {
        this.jwtProvider = jwtProvider;
    }


    @Bean
    public HiddenHttpMethodFilter hiddenHttpMethodFilter() {
        return new HiddenHttpMethodFilter();
    }

    @Bean
    @Order(1)
    public JwtInterceptor jwtInterceptor() {
        return new JwtInterceptor(jwtProvider);
    }

    @Bean
    @Order(2)
    public AuthorizationInterceptor authorizationInterceptor() {

        return new AuthorizationInterceptor();}

    @Bean
    @Order(3)
    public AdminInterceptor adminInterceptor() {
        return new AdminInterceptor(jwtProvider);}

    @Bean
    public LoginMemberArgumentResolver loginMemberArgumentResolver() {
        return new LoginMemberArgumentResolver();
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(jwtInterceptor())
                .excludePathPatterns(
                        "/api/oauth/**",
                        "/api/members/**");
        registry.addInterceptor(authorizationInterceptor())
                .addPathPatterns("/api/admin/**");
        registry.addInterceptor(adminInterceptor())
                .addPathPatterns("/admin/**");
        WebMvcConfigurer.super.addInterceptors(registry);
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(loginMemberArgumentResolver());
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("*")
                .allowedMethods("GET", "POST", "PUT", "DELETE")
                .allowedHeaders("Authorization", "Content-Type")
                .exposedHeaders("Authorization")
                .maxAge(3600);
    }
}
