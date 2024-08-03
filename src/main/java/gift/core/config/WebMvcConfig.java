package gift.core.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import gift.core.interceptor.AuthenticationInterceptor;
import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
public class WebMvcConfig implements WebMvcConfigurer {
	private final AuthenticationInterceptor authenticationInterceptor;

	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(authenticationInterceptor)
			.excludePathPatterns("/css/**", "/images/**", "/js/**")
			.excludePathPatterns("/api/v1/user/signup")
			.excludePathPatterns("/api/v1/user/login");
	}

	@Override
	public void addCorsMappings(CorsRegistry registry) {
		registry.addMapping("/api/**")
			.allowedOrigins("http://localhost:3000")
			.allowedMethods("GET", "POST", "PUT", "DELETE")
			.allowedHeaders("*")
			.allowCredentials(true);
	}
}
