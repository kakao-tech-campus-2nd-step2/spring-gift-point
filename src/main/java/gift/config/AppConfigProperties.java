package gift.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "app")
public class AppConfigProperties {

    private int devConnectTimeout;
    private int devReadTimeout;
    private int prodConnectTimeout;
    private int prodReadTimeout;

    public int getDevConnectTimeout() {
        return devConnectTimeout;
    }

    public void setDevConnectTimeout(int devConnectTimeout) {
        this.devConnectTimeout = devConnectTimeout;
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
