package gift.config;

import gift.filter.AuthFilter;
import gift.repository.token.TokenSpringDataJpaRepository;
import jakarta.servlet.Filter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FilterConfiguration {

    private final TokenSpringDataJpaRepository tokenRepository;

    @Autowired
    public FilterConfiguration(TokenSpringDataJpaRepository tokenRepository) {
        this.tokenRepository = tokenRepository;
    }

    @Bean(name = "authFilterBean")
    public FilterRegistrationBean<Filter> authFilter() {
        FilterRegistrationBean<Filter> filterRegistrationBean = new FilterRegistrationBean<>();
        filterRegistrationBean.setFilter(new AuthFilter(tokenRepository));
        filterRegistrationBean.setOrder(1);
        filterRegistrationBean.addUrlPatterns("/*");
        return filterRegistrationBean;
    }

}
