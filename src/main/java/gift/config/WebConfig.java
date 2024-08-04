package gift.config;

import gift.interceptor.MemberInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    private final MemberInterceptor memberInterceptor;

    public WebConfig(MemberInterceptor memberInterceptor) {
        this.memberInterceptor = memberInterceptor;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(memberInterceptor)
            .addPathPatterns("/api/wishes/**", "/oauth/kakao/**", "/api/orders/**", "/api/members/point")
            .excludePathPatterns("/oauth/kakao", "/oauth/kakao/callback");
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
            .allowedOriginPatterns("*")
            .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS", "HEAD", "PATCH")
            .allowedHeaders("*")
            .exposedHeaders(HttpHeaders.LOCATION)
            .allowCredentials(true)
            .maxAge(3600);
    }
}
