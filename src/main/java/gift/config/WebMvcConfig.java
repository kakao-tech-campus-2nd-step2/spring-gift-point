package gift.config;

import gift.resolver.AuthHeaderManager;
import gift.resolver.LoginMemberArgumentResolver;
import gift.service.MemberService;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    private final MemberService memberService;
    private final JwtProvider jwtProvider;
    private final AuthHeaderManager authHeaderManager;

    public WebMvcConfig(MemberService memberService, JwtProvider jwtProvider, AuthHeaderManager authHeaderManager) {
        this.memberService = memberService;
        this.jwtProvider = jwtProvider;
        this.authHeaderManager = authHeaderManager;
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(new LoginMemberArgumentResolver(memberService, jwtProvider, authHeaderManager));
    }

}
