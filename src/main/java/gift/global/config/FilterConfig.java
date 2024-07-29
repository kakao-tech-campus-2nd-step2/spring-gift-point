package gift.global.config;

import gift.global.security.JwtFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FilterConfig {

    private final JwtFilter jwtFilter;

    public FilterConfig(JwtFilter jwtFilter) {
        this.jwtFilter = jwtFilter;
    }

    @Bean
    public FilterRegistrationBean<JwtFilter> jwtFilterFilterRegistrationBean() {
        FilterRegistrationBean<JwtFilter> registrationBean = new FilterRegistrationBean<>();
        registrationBean.setFilter(jwtFilter);

        registrationBean.addUrlPatterns("/members/*");
        registrationBean.addUrlPatterns("/api/*");
        registrationBean.addUrlPatterns("/wishes/*");
        registrationBean.addUrlPatterns("/products/*");

        return registrationBean;
    }

}
