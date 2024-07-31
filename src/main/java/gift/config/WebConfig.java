package gift.config;

import gift.jwt.JwtTokenProvider;
import gift.service.MemberService;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    private LoginMemberArgumentResolver loginMemberArgumentResolver;
    private final MemberService memberService;
    private final JwtTokenProvider jwtTokenProvider;

    public WebConfig(MemberService memberService, JwtTokenProvider jwtTokenProvider, LoginMemberArgumentResolver loginMemberArgumentResolver) {
        this.memberService = memberService;
        this.jwtTokenProvider = jwtTokenProvider; 
        this.loginMemberArgumentResolver = loginMemberArgumentResolver;
    }
    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
      
        resolvers.add(new LoginMemberArgumentResolver(memberService, jwtTokenProvider));
    }
}