package gift.utils.config;

import gift.repository.TokenRepository;
import gift.utils.ExternalApiService;
import gift.utils.JwtTokenProvider;
import gift.utils.filter.AuthFilter;
import gift.utils.filter.OAuthFilter;
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
    public FilterRegistrationBean<AuthFilter> authFilter() {
        FilterRegistrationBean<AuthFilter> registrationBean = new FilterRegistrationBean<>();
        registrationBean.setFilter(new AuthFilter(jwtTokenProvider));
        registrationBean.addUrlPatterns("/*"); // 모든 경로에 필터 적용
        registrationBean.setOrder(1);
        return registrationBean;
    }

    @Bean
    public FilterRegistrationBean<OAuthFilter> oauthFilter() {
        FilterRegistrationBean<OAuthFilter> registrationBean = new FilterRegistrationBean<>();
        registrationBean.setFilter(new OAuthFilter(jwtTokenProvider,tokenRepository,externalApiService));
        registrationBean.addUrlPatterns("/*"); // 모든 경로에 필터 적용
        registrationBean.setOrder(2);
        return registrationBean;
    }


}
