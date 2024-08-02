package gift.config;

import gift.auth.AuthApiInterceptor;
import gift.auth.AuthMvcInterceptor;
import gift.auth.JwtTokenProvider;
import gift.auth.OAuthService;
import gift.resolver.LoginMemberArgumentResolver;
import java.util.List;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class LoginWebConfig implements WebMvcConfigurer {

    private final JwtTokenProvider jwtTokenProvider;
    private final OAuthService oAuthService;

    public LoginWebConfig(JwtTokenProvider jwtTokenProvider, OAuthService oAuthService) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.oAuthService = oAuthService;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new AuthApiInterceptor(jwtTokenProvider, oAuthService))
            .order(1)
            .addPathPatterns("/api/**")
            .excludePathPatterns("/api/members/register", "/api/members/login",
                "/api/oauth2/kakao", "/view/**");
        registry.addInterceptor(new AuthMvcInterceptor(jwtTokenProvider))
            .order(2)
            .addPathPatterns("/view/**")
            .excludePathPatterns("/api/**","/view/products", "/view/join",
                "/view/login");

    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(new LoginMemberArgumentResolver());
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**") // 서버에서 CORS를 적용할 uri 패턴
            .allowedOriginPatterns("*") // CORS를 지원할 클라이언트 Origin
            .allowedMethods("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS")
            .allowedHeaders("*") // CORS에 허용되는 요청 헤더
            .allowCredentials(true) // 클라이언트에 대한 응답에 credentials(인증 헤더, 쿠키)를 포함할 수 있는 여부
            .exposedHeaders("Location") // 클라이언트 측에 전달되는 응답에 노출을 허용하게 하는 헤더
            .maxAge(1800); // 예비 요청(options)의 결과값을 캐싱해놓을 수 있는 시간 (cross-origin 요청 시 캐싱에 의해 예비 요청 생략 가능)
    }
}
