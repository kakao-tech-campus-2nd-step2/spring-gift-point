package gift;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.bind.ConstructorBinding;

@ConfigurationProperties("kakao.oauth")
public class KakaoProperties {

    private final String clientId;
    private final String redirectUri;
    private final String tokenUrl;
    private final String userInfoUrl;
    private final String sendMessageUrl;
    private final String baseUrl;

    // Constructor
    public KakaoProperties(String clientId, String redirectUri, String tokenUrl, String userInfoUrl, String sendMessageUrl, String baseUrl) {
        this.clientId = clientId;
        this.redirectUri = redirectUri;
        this.tokenUrl = tokenUrl;
        this.userInfoUrl = userInfoUrl;
        this.sendMessageUrl = sendMessageUrl;
        this.baseUrl = baseUrl;
    }

    // Getters
    public String getClientId() {
        return clientId;
    }

    public String getRedirectUri() {
        return redirectUri;
    }

    public String getTokenUrl() {
        return tokenUrl;
    }

    public String getUserInfoUrl() {
        return userInfoUrl;
    }

    public String getSendMessageUrl() {
        return sendMessageUrl;
    }
    public String getBaseUrl() {
        return baseUrl;
    }
}