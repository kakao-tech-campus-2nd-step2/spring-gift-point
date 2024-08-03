package gift.Config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CORSConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
            .allowedOrigins("*") // 모든 origin 추가
            .allowedMethods("GET", "POST", "PUT" ,"DELETE","OPTIONS", "PATCH") // 프론트에서 쓰이는 허용할 HTTP 메서드 OPTIONS 추가
            .allowedHeaders("*") // 모든 헤더를 허용
            .allowCredentials(true) // Bearer token으로 인증을 하기 때문에 true
            .maxAge(1800L); // 과제 힌트에서 1800초로 설정되어 있어 1800초로 설정

    }
}
