package gift.config;

import gift.filter.JwtAuthenticationFilter;
import gift.util.JwtUtil;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FilterConfig {

    private final JwtUtil jwtUtil;

    public FilterConfig(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @Bean
    public FilterRegistrationBean<JwtAuthenticationFilter> filterBean(){
        FilterRegistrationBean<JwtAuthenticationFilter> registrationBean = new FilterRegistrationBean<>(new JwtAuthenticationFilter(jwtUtil));
        registrationBean.addUrlPatterns("/api/wishlist/*");
        registrationBean.addUrlPatterns("/api/orders/*");
        return registrationBean;
    }
}
