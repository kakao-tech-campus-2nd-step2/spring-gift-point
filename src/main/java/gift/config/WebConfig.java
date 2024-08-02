package gift.config;

import feign.Client;
import gift.authentication.filter.AuthenticationExceptionHandlerFilter;
import gift.authentication.filter.AuthenticationFilter;
import gift.authentication.token.JwtResolver;
import gift.web.resolver.LoginMemberArgumentResolver;
import java.util.List;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Value("${cors.exposed-headers}")
    public String EXPOSED_HEADERS;

    @Value("${cors.allowed-method-names}")
    public String ALLOWED_METHOD_NAMES;

    private final LoginMemberArgumentResolver loginUserArgumentResolver;

    private final JwtResolver jwtResolver;

    public WebConfig(LoginMemberArgumentResolver loginUserArgumentResolver, JwtResolver jwtResolver) {
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
        filterRegistrationBean.setFilter(new AuthenticationFilter(jwtResolver));
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
        registry.addMapping("/**")
            .allowedOrigins("http://localhost:3000")
            .allowedMethods(ALLOWED_METHOD_NAMES.split(","))
            .allowedHeaders("*")
            .exposedHeaders(EXPOSED_HEADERS.split(","))
            .maxAge(1800)
            .allowCredentials(true);
    }
}
