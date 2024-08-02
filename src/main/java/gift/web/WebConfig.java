package gift.web;

import gift.service.member.MemberService;
import gift.web.jwt.AuthUserArgumentResolver;
import gift.web.jwt.JwtUtils;
import java.util.List;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    private final MemberService memberService;
    private final JwtUtils jwtUtils;

    public WebConfig(MemberService memberService, JwtUtils jwtUtils) {
        this.memberService = memberService;
        this.jwtUtils = jwtUtils;
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(new AuthUserArgumentResolver(memberService, jwtUtils));
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
            .allowedOrigins("*")
            .maxAge(3600)
            .allowedHeaders("*")
            .allowedMethods("POST", "GET", "PUT", "OPTIONS", "DELETE");

    }
}
