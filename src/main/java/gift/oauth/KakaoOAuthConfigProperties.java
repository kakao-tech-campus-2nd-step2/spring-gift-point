package gift.oauth;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("kakao")
public class KakaoOAuthConfigProperties {
    private final String clientId;
    private final String redirectUrl;

    public KakaoOAuthConfigProperties(String clientId, String redirectUrl) {
        this.clientId = clientId;
        this.redirectUrl = redirectUrl;
    }

    public String getClientId() {
        return clientId;
    }

    public String getRedirectUrl() {
        return redirectUrl;
    }
}
