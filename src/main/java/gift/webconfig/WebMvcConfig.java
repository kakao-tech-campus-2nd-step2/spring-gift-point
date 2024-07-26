package gift.webconfig;

import gift.argumentresolver.AuthArgumentResolver;
import gift.argumentresolver.KakaoAuthArgumentResolver;
import gift.security.JwtUtil;
import gift.service.KakaoApiService;
import java.util.List;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

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
}
