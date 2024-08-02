package gift.config;

import gift.argumentresolver.AuthArgumentResolver;
import gift.argumentresolver.KakaoAuthArgumentResolver;
import gift.service.JwtUtil;
import gift.service.KakaoApiService;
import java.util.List;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.config.annotation.CorsRegistry;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {
    private final JwtUtil jwtUtil;
    private final KakaoApiService kakaoApiService;
    public WebMvcConfig(JwtUtil jwtUtil, KakaoApiService kakaoApiService) {
        this.jwtUtil = jwtUtil;
        this.kakaoApiService = kakaoApiService;
    }
    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(new AuthArgumentResolver(jwtUtil));
        resolvers.add(new KakaoAuthArgumentResolver(kakaoApiService));
    }
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
            .allowedOrigins("http://kakaogift.s3-website.ap-northeast-2.amazonaws.com")
            .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
            .allowCredentials(true);
    }
}
