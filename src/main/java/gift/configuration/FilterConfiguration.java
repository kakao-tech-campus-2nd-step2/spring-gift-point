package gift.configuration;

import gift.filter.AuthFilter;
import gift.filter.LoginFilter;
import gift.filter.MyTokenFilter;
import gift.filter.OAuthTokenFilter;
import gift.repository.token.TokenRepository;
import jakarta.servlet.Filter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FilterConfiguration {

    private final TokenRepository tokenRepository;

    public FilterConfiguration(TokenRepository tokenRepository) {
        this.tokenRepository = tokenRepository;
    }

    @Bean
    public FilterRegistrationBean<Filter> authFilter(){
        FilterRegistrationBean<Filter> filterRegistrationBean = new FilterRegistrationBean<>();
        filterRegistrationBean.setFilter(new AuthFilter(tokenRepository));
        filterRegistrationBean.setOrder(1);
        filterRegistrationBean.addUrlPatterns("/*");

        return filterRegistrationBean;
    }

    @Bean
    public FilterRegistrationBean<Filter> myTokenFilter(){
        FilterRegistrationBean<Filter> filterRegistrationBean = new FilterRegistrationBean<>();
        filterRegistrationBean.setFilter(new MyTokenFilter(tokenRepository));
        filterRegistrationBean.setOrder(2);
        filterRegistrationBean.addUrlPatterns("/*");

        return filterRegistrationBean;
    }

    @Bean
    public FilterRegistrationBean<Filter> oauthTokenFilter(){
        FilterRegistrationBean<Filter> filterRegistrationBean = new FilterRegistrationBean<>();
        filterRegistrationBean.setFilter(new OAuthTokenFilter(tokenRepository));
        filterRegistrationBean.setOrder(3);
        filterRegistrationBean.addUrlPatterns("/*");

        return filterRegistrationBean;
    }

    @Bean
    public FilterRegistrationBean<Filter> loginFilter(){
        FilterRegistrationBean<Filter> filterRegistrationBean = new FilterRegistrationBean<>();
        filterRegistrationBean.setFilter(new LoginFilter(tokenRepository));
        filterRegistrationBean.setOrder(4);
        filterRegistrationBean.addUrlPatterns("/members/login");
        filterRegistrationBean.addUrlPatterns("/members/login/oauth/kakao");

        return filterRegistrationBean;
    }

}
