package gift.jwt;

import java.util.List;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(new LoginMethodArgumentResolver(new JwtService()));//LoginMemberArgumentResolver 등록
        resolvers.add(new OAuthMethodArgumentResolver(new JwtService()));
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new JwtInterceptor(new JwtService()))
            .addPathPatterns("/wish", "api/order"); // 필요한 경로 설정
        //.excludePathPatterns("/signup", "/login"); // 회원가입과 로그인은 토큰 없어도 됨
    }
}
