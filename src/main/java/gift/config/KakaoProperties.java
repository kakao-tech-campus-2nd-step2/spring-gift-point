package gift.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;

@Configuration
@ConfigurationProperties(prefix = "kakao")
public class KakaoProperties {
    private String clientId;
    private String redirectUrl;
    private String loginUrl;
    private final int connectTimeout;
    private final int readTimeout;

    public KakaoProperties() {
        this.connectTimeout = (int) Duration.ofSeconds(1L).toMillis();
        this.readTimeout = (int) Duration.ofSeconds(5L).toMillis();
    }

    public int getConnectTimeout() {
        return connectTimeout;
    }

    public int getReadTimeout() {
        return readTimeout;
    }

    public String getClientId() {
        return clientId;
    }

    public String getRedirectUrl() {
        return redirectUrl;
    }

    public String getLoginUrl() {
        return loginUrl;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public void setRedirectUrl(String redirectUrl) {
        this.redirectUrl = redirectUrl;
    }

    public void setLoginUrl(String loginUrl) {
        this.loginUrl = loginUrl;
    }
}
