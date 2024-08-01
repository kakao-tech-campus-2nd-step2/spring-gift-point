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
        registry.addMapping("/**")
                .allowedOrigins("*")
                .allowedMethods("*")
                .allowedHeaders("*")
                .exposedHeaders("Location")
                .maxAge(3600);
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(tokenInterceptor)
                .addPathPatterns("/api/wishes/**","/api/orders/**")
                .excludePathPatterns("/api/members/**","/products/**","/api/categories/**","/api/products/**");
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(new LoginUserArgumentResolver(memberService, jwtUtil, kakaoService));
    }
}
