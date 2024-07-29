package gift;

import gift.Model.KakaoProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@EnableConfigurationProperties(KakaoProperties.class)
@ConfigurationPropertiesScan
@SpringBootApplication()
public class Application   {
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
