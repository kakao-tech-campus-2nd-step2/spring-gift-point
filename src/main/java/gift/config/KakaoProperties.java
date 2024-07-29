package gift.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "kakao")
public class KakaoProperties {
    private String clientId;
    private String redirectUrl;
    private String loginUrl;

    public KakaoProperties() {
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
