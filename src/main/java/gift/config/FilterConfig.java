package gift.config;

import gift.filter.AdminFilter;
import gift.filter.JwtFilter;
import gift.repository.UserRepository;
import gift.util.UserUtility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FilterConfig {

    @Value("${spring.var.token-prefix}")
    private String tokenPrefix;

    private final UserUtility userUtility;
    private final UserRepository userRepository;

    @Autowired
    public FilterConfig(UserUtility userUtility, UserRepository userRepository) {
        this.userUtility = userUtility;
        this.userRepository = userRepository;
    }

    private static final String[] JWT_URL_PATTERNS = {
            "/api/products/*",
            "/api/orders/*",
            "/api/wishes/*",
            "/api/categories/*",
            "/api/members/me"
    };

    private static final String[] ADMIN_URL_PATTERNS = {
            "/api/members/charge",
            "/api/pointHistories/*"
    };

    @Bean
    public FilterRegistrationBean<JwtFilter> jwtFilter() {
        FilterRegistrationBean<JwtFilter> registrationBean = new FilterRegistrationBean<>();
        registrationBean.setFilter(new JwtFilter(tokenPrefix, userUtility));
        for (String url : JWT_URL_PATTERNS) {
            registrationBean.addUrlPatterns(url);
        }
        return registrationBean;
    }

    @Bean
    public FilterRegistrationBean<AdminFilter> adminFilter() {
        FilterRegistrationBean<AdminFilter> registrationBean = new FilterRegistrationBean<>();
        registrationBean.setFilter(new AdminFilter(tokenPrefix, userUtility));
        for (String url : ADMIN_URL_PATTERNS) {
            registrationBean.addUrlPatterns(url);
        }
        return registrationBean;
    }
}
