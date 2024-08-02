package gift.common.config;

import gift.common.auth.JwtUtil;
import gift.common.auth.LoginMemberArgumentResolver;
import gift.member.repository.MemberRepository;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    private final JwtUtil jwtUtil;
    private final MemberRepository memberRepository;

    public WebConfig(JwtUtil jwtUtil, MemberRepository memberRepository) {
        this.jwtUtil = jwtUtil;
        this.memberRepository = memberRepository;
    }

    // LoginMember인지 판별하기
    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(new LoginMemberArgumentResolver(jwtUtil, memberRepository));
    }

    // CORS 설정 추가
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("*")  // 모든 출처 허용
                .allowedMethods("*")  // 모든 HTTP 메서드 허용
                .allowedHeaders("*")  // 모든 헤더 허용
                .maxAge(3600);  // pre-flight 요청 캐시 시간
    }
}
