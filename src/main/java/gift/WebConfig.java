package gift;

import gift.Service.MemberService;
import gift.annotation.ValidUserArgumnetResolver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    private final MemberService memberService;

    @Autowired
    public WebConfig(MemberService memberService) {
        this.memberService = memberService;
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        // 위시리스트 API에 대한 CORS 설정
        registry.addMapping("/api/wishes/**")
                .allowedOrigins("아직 프론트분들이 배포가 안되서 추후 채울 예정")
                .allowedMethods("GET", "POST", "DELETE", "OPTIONS")
                .allowedHeaders("Authorization", "Content-Type")
                .allowCredentials(true)
                .maxAge(1800);

        // 주문 API에 대한 CORS 설정
        registry.addMapping("/api/orders/**")
                .allowedOrigins("아직 프론트분들이 배포가 안되서 추후 채울 예정")
                .allowedMethods("POST", "PUT", "OPTIONS")
                .allowedHeaders("Authorization", "Content-Type")
                .allowCredentials(true)
                .maxAge(1800);

        registry.addMapping("/api/**")
                .allowedOrigins("*")
                .allowedMethods("*")
                .allowedHeaders("*")
                .maxAge(1800);
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
        argumentResolvers.add(new ValidUserArgumnetResolver(memberService));
    }

}
