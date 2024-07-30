package gift.config;

import gift.filter.TokenInterceptor;
import gift.repository.KakaoUserRepository;
import gift.repository.UserRepository;
import gift.resolver.LoginMemberArgumentResolver;
import gift.service.TokenService;
import gift.util.AuthorizationHeaderProcessor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    private final TokenInterceptor tokenInterceptor;
    private final UserRepository userRepository;
    private final KakaoUserRepository kakaoUserRepository;
    private final TokenService tokenService;
    private final AuthorizationHeaderProcessor authorizationHeaderProcessor;

    public WebConfig(TokenInterceptor tokenInterceptor, UserRepository userRepository, KakaoUserRepository kakaoUserRepository, TokenService tokenService, AuthorizationHeaderProcessor authorizationHeaderProcessor) {
        this.tokenInterceptor = tokenInterceptor;
        this.userRepository = userRepository;
        this.kakaoUserRepository = kakaoUserRepository;
        this.tokenService = tokenService;
        this.authorizationHeaderProcessor = authorizationHeaderProcessor;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(tokenInterceptor)
                .addPathPatterns("/api/wishes")
                .excludePathPatterns("/api/members/login", "/api/members/register");
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(new LoginMemberArgumentResolver(userRepository, kakaoUserRepository, tokenService, authorizationHeaderProcessor));
    }
}
