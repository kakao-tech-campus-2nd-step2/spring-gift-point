package gift.utils.config;

import gift.repository.TokenRepository;
import gift.utils.ExternalApiService;
import gift.utils.JwtTokenProvider;
import gift.utils.filter.AuthFilter;
import gift.utils.filter.OAuthFilter;
import gift.utils.filter.CorsFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FilterConfig {
    private final JwtTokenProvider jwtTokenProvider;
    private final TokenRepository tokenRepository;
    private final ExternalApiService externalApiService;

    public FilterConfig(JwtTokenProvider jwtTokenProvider, TokenRepository tokenRepository,
        ExternalApiService externalApiService) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.tokenRepository = tokenRepository;
        this.externalApiService = externalApiService;
    }

    @Bean
    public FilterRegistrationBean<CorsFilter> corsFilter() {
        FilterRegistrationBean<CorsFilter> registrationBean = new FilterRegistrationBean<>();
        registrationBean.setFilter(new CorsFilter());
        registrationBean.addUrlPatterns("/*");
        registrationBean.setOrder(0); // CORS 필터를 가장 먼저 적용
        return registrationBean;
    }

    @Bean
    public FilterRegistrationBean<AuthFilter> authFilter() {
        FilterRegistrationBean<AuthFilter> registrationBean = new FilterRegistrationBean<>();
        registrationBean.setFilter(new AuthFilter(jwtTokenProvider));
        registrationBean.addUrlPatterns("/*");
        registrationBean.setOrder(1);
        return registrationBean;
    }

    @Bean
    public FilterRegistrationBean<OAuthFilter> oauthFilter() {
        FilterRegistrationBean<OAuthFilter> registrationBean = new FilterRegistrationBean<>();
        registrationBean.setFilter(new OAuthFilter(jwtTokenProvider, tokenRepository, externalApiService));
        registrationBean.addUrlPatterns("/*");
        registrationBean.setOrder(2);
        return registrationBean;
    }
}
