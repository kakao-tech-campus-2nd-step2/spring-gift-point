package gift.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig {

    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/api/orders")
                    .allowedOriginPatterns("*")
                    .allowedMethods("POST")
                    .allowedHeaders("Authorization")
                    .allowCredentials(true);

                registry.addMapping("/members/**")
                    .allowedOrigins("*")
                    .allowedMethods("POST");

                registry.addMapping("/members/point")
                    .allowedOriginPatterns("*")
                    .allowedMethods("GET")
                    .allowedHeaders("Authorization")
                    .allowCredentials(true);

                registry.addMapping("/api/**")
                    .allowedOrigins("*")
                    .allowedMethods("GET");

                registry.addMapping("/wishes/**")
                    .allowedOriginPatterns("*")
                    .allowedMethods("GET", "POST", "DELETE")
                    .allowedHeaders("Authorization")
                    .allowCredentials(true);
            }
        };
    }
}
