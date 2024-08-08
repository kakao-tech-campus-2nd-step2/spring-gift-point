package gift.config;

import feign.Client;
import gift.authentication.config.CorsConfigurationProperties;
import gift.authentication.filter.AuthenticationExceptionHandlerFilter;
import gift.authentication.filter.AuthenticationFilter;
import gift.authentication.token.JwtResolver;
import gift.web.resolver.LoginMemberArgumentResolver;
import java.util.List;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    private final CorsConfigurationProperties corsProperties;

    private final LoginMemberArgumentResolver loginUserArgumentResolver;

    private final JwtResolver jwtResolver;

    private final List<String> IgnoreFilterPaths = List.of(
        "/api/members/login",
        "/api/members/register",
        "/api/login/oauth2/kakao");

    private final List<String> ignorePathsOnlyMethodGet = List.of(
        "/api/categories",
        "/api/products",
        "/api/products/\\d+",
        "/api/products/\\d+/options");

    public WebConfig(CorsConfigurationProperties corsProperties, LoginMemberArgumentResolver loginUserArgumentResolver, JwtResolver jwtResolver) {
        this.corsProperties = corsProperties;
        this.loginUserArgumentResolver = loginUserArgumentResolver;
        this.jwtResolver = jwtResolver;
    }

    @Bean
    public FilterRegistrationBean authenticationExceptionHandlerFilter() {
        FilterRegistrationBean filterRegistrationBean = new FilterRegistrationBean();
        filterRegistrationBean.setFilter(new AuthenticationExceptionHandlerFilter());
        filterRegistrationBean.setOrder(1);
        return filterRegistrationBean;
    }

    @Bean
    public FilterRegistrationBean authenticationFilter() {
        FilterRegistrationBean filterRegistrationBean = new FilterRegistrationBean();
        filterRegistrationBean.setFilter(new AuthenticationFilter(jwtResolver, IgnoreFilterPaths, ignorePathsOnlyMethodGet));
        filterRegistrationBean.addUrlPatterns("/api/*");
        filterRegistrationBean.setOrder(2);
        return filterRegistrationBean;
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(loginUserArgumentResolver);
    }

    @Bean
    public Client feignClient() {
        return new Client.Default(null, null);
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping(corsProperties.getPathPatterns())
            .allowedOrigins(corsProperties.getOriginPatterns())
            .allowedMethods(corsProperties.getAllowedMethods())
            .allowedHeaders(corsProperties.getAllowedHeaders())
            .allowCredentials(corsProperties.getAllowCredentials())
            .exposedHeaders(corsProperties.getExposedHeaders())
            .maxAge(corsProperties.getMaxAge());
    }
}
