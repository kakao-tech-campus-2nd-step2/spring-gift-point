package gift.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "app.dev")
public class AppConfigProperties {

    @Value("${app.dev.connect.timeout}")
    private int devConnectTimeout;

    @Value("${app.dev.read.timeout}")
    private int devReadTimeout;

    @Value("${app.prod.connect.timeout}")
    private int prodConnectTimeout;

    @Value("${app.prod.read.timeout}")
    private int prodReadTimeout;

    public int getDevConnectTimeout() {
        return devConnectTimeout;
    }

    public int getDevReadTimeout() {
        return devReadTimeout;
    }

    public int getProdConnectTimeout() {
        return prodConnectTimeout;
    }

    public int getProdReadTimeout() {
        return prodReadTimeout;
    }
}