package gift;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.retry.annotation.EnableRetry;


@ConfigurationPropertiesScan
@SpringBootApplication
@EnableJpaAuditing
@EnableRetry
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

}
