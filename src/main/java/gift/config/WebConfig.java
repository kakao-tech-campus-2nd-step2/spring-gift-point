package gift.config;

import gift.interceptor.AuthInterceptor;
import gift.repository.MemberRepository;
import gift.service.MemberService;
import gift.util.JwtUtil;
import gift.util.LoginMemberArgumentResolver;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    private final JwtUtil jwtUtil;
    private final MemberService memberService;
    private final AuthInterceptor authInterceptor;

    public WebConfig(JwtUtil jwtUtil, MemberRepository memberRepository, MemberService memberService, AuthInterceptor authInterceptor) {
        this.jwtUtil = jwtUtil;
        this.memberService = memberService;
        this.authInterceptor = authInterceptor;
    }

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/").setViewName("redirect:/products");
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(new LoginMemberArgumentResolver(jwtUtil, memberService));
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(authInterceptor)
                .addPathPatterns("/api/orders/**");
    }
}
