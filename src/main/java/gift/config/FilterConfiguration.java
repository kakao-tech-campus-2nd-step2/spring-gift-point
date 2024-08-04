package gift.config;

import gift.filter.AuthFilter;
import gift.repository.token.TokenSpringDataJpaRepository;
import gift.service.TokenService;
import jakarta.servlet.Filter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FilterConfiguration {

    private final TokenService tokenService;

    @Autowired
    public FilterConfiguration(TokenSpringDataJpaRepository tokenRepository, TokenService tokenService) {
        this.tokenService = tokenService;
    }

    @Bean(name = "authFilterBean")
    public FilterRegistrationBean<Filter> authFilter() {
        FilterRegistrationBean<Filter> filterRegistrationBean = new FilterRegistrationBean<>();
        filterRegistrationBean.setFilter(new AuthFilter(tokenService));
        filterRegistrationBean.setOrder(1);
        filterRegistrationBean.addUrlPatterns("/*");
        return filterRegistrationBean;
    }

}
