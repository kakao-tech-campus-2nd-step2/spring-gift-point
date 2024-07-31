package gift.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "app")
public class AppConfigProperties {
    private int devConnectTimeout;
    private int devReadTimeout;
    private int prodConnectTimeout;
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