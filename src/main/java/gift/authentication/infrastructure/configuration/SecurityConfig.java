package gift.authentication.infrastructure.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SecurityConfig {
    private final CustomJwtFilter customJwtFilter;

    @Autowired
    public SecurityConfig(CustomJwtFilter customJwtFilter) {
        this.customJwtFilter = customJwtFilter;
    }

    @Bean
    public FilterRegistrationBean<CustomJwtFilter> jwtFilter() {
        FilterRegistrationBean<CustomJwtFilter> registrationBean = new FilterRegistrationBean<>();
        registrationBean.setFilter(customJwtFilter);
        registrationBean.addUrlPatterns("*");
        return registrationBean;
    }
}
