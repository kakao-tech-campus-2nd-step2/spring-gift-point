package gift.config;


import gift.annotation.KakaoUserArgumentResolver;
import gift.annotation.LoginUserArgumentResolver;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    private final LoginUserArgumentResolver loginUserArgumentResolver;
    private final KakaoUserArgumentResolver kakaoUserArgumentResolver;

    public WebConfig(LoginUserArgumentResolver loginUserArgumentResolver, KakaoUserArgumentResolver kakaoUserArgumentResolver) {
        this.loginUserArgumentResolver = loginUserArgumentResolver;
        this.kakaoUserArgumentResolver = kakaoUserArgumentResolver;
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
        argumentResolvers.add(loginUserArgumentResolver);
        argumentResolvers.add(kakaoUserArgumentResolver);
    }
}