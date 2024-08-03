package gift.global.configuration;

import gift.global.component.AdminLoginInterceptor;
import gift.global.component.LoginInterceptor;
import gift.global.resolver.UserIdResolver;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

// WebMvcConfigurer를 구현하는 클래스.
@Configuration
@SpringBootConfiguration
public class WebMvcConfig implements WebMvcConfigurer {

    private final LoginInterceptor loginInterceptor;
    private final AdminLoginInterceptor adminLoginInterceptor;
    private final UserIdResolver userIdResolver;

    @Autowired
    public WebMvcConfig(LoginInterceptor loginInterceptor,
        AdminLoginInterceptor adminLoginInterceptor, UserIdResolver userIdResolver) {
        this.loginInterceptor = loginInterceptor;
        this.adminLoginInterceptor = adminLoginInterceptor;
        this.userIdResolver = userIdResolver;
    }

    // 인터셉터를 추가하는 메서드를 재정의하여 loginInterceptor를 등록하도록 함.
    // 이전까지는 토큰 검증만 했다면, 이제는 토큰 검증에 실패 시 로그인 화면으로 연계하도록 해야 함.
    // 그러나 front 페이지를 만들지 않았으므로 ApiResponse로 Error를 반환하도록 하였습니다.
    @Override
    public void addInterceptors(InterceptorRegistry interceptorRegistry) {
        // login 인터셉터 추가
        interceptorRegistry
            .addInterceptor(loginInterceptor)
            .order(1)

            // 모든 행동에 대해 검증
            .addPathPatterns("/api/**")

            // 로그인만 제외하고
            .excludePathPatterns("/api/login/**");

        // admin 인터셉터 추가
        interceptorRegistry
            .addInterceptor(adminLoginInterceptor)
            .order(2)

            // admin 관련 행동에 대해 검증
            .addPathPatterns("/admin/**")
            .addPathPatterns("/api/admin/**");
    }

    // argument resolver를 추가하는 메서드 재정의
    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(userIdResolver);
    }

    // 배포 시의 CORS 설정을 위한 메서드 재정의
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
            .allowedOrigins("*");
    }
}
