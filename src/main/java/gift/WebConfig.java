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
                .allowedOrigins("https://minji2219.github.io/")
                .allowedMethods("GET", "POST", "DELETE", "OPTIONS")
                .allowedHeaders("Authorization", "Content-Type")
                .allowCredentials(true)
                .maxAge(1800);

        // 주문 API에 대한 CORS 설정
        registry.addMapping("/api/orders/**")
                .allowedOrigins("https://minji2219.github.io/")
                .allowedMethods("POST", "OPTIONS")
                .allowedHeaders("Authorization", "Content-Type")
                .allowCredentials(true)
                .maxAge(1800);


        //멤버 포인트 조회 API에 대한 CORS 설정
        registry.addMapping("/api/members/point")
                .allowedOrigins("https://minji2219.github.io/")
                .allowedMethods("GET", "OPTIONS")
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
