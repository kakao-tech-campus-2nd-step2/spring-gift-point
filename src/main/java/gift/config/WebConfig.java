package gift.config;

import gift.filter.JwtTokenFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import jakarta.servlet.Filter;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;


@Configuration
public class WebConfig {

    private final JwtTokenFilter jwtTokenFilter;

    @Autowired
    public WebConfig(JwtTokenFilter jwtTokenFilter) {
        this.jwtTokenFilter = jwtTokenFilter;
    }

    @Bean

    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    @Bean

    public FilterRegistrationBean<Filter> jwtFilter() {
        FilterRegistrationBean<Filter> registrationBean = new FilterRegistrationBean<>();
        registrationBean.setFilter(jwtTokenFilter);


        registrationBean.addUrlPatterns("/api/products/delete/*");
        return registrationBean;
    }
}
