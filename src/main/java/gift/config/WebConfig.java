package gift.config;

import gift.interceptor.TokenInterceptor;
import gift.resolver.LoginUserArgumentResolver;
import gift.service.JwtUtil;
import gift.service.KakaoService;
import gift.service.MemberService;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.http.HttpHeaders;
import org.springframework.web.servlet.config.annotation.CorsRegistry;

import java.util.List;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    private  final MemberService memberService;
    private final TokenInterceptor tokenInterceptor;
    private final JwtUtil jwtUtil;
    private final KakaoService kakaoService;

    public WebConfig(MemberService memberService, TokenInterceptor tokenInterceptor, JwtUtil jwtUtil, KakaoService kakaoService) {
        this.memberService = memberService;
        this.tokenInterceptor = tokenInterceptor;
        this.jwtUtil = jwtUtil;
        this.kakaoService = kakaoService;
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/api/**")
                .allowedOrigins("http://localhost:3000") // 나중에 지정 예정
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                .allowedHeaders("*")
                .allowCredentials(true)
                .exposedHeaders(HttpHeaders.LOCATION);
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(tokenInterceptor)
                .addPathPatterns("/api/**")
                .excludePathPatterns("/api/members/register", "/api/members/login","/products/**");
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(new LoginUserArgumentResolver(memberService, jwtUtil, kakaoService));
    }
}
