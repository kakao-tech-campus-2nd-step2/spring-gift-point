package gift.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.bind.ConstructorBinding;

@ConfigurationProperties(prefix = "kakao")
public class KakaoProperties {

    private final String clientId;
    private final String redirectUri;
    private final String authUrl;
    private final String tokenUrl;
    private final String infoUrl;
    private final String messageUrl;
    private final int connectTimeoutMillis;
    private final int responseTimeoutSeconds;

    public KakaoProperties(String clientId, String redirectUri, String authUrl, String tokenUrl, String infoUrl, String messageUrl, int connectTimeoutMillis, int responseTimeoutSeconds) {
        this.clientId = clientId;
        this.redirectUri = redirectUri;
        this.authUrl = authUrl;
        this.tokenUrl = tokenUrl;
        this.infoUrl = infoUrl;
        this.messageUrl = messageUrl;
        this.connectTimeoutMillis = connectTimeoutMillis;
        this.responseTimeoutSeconds = responseTimeoutSeconds;
    }

    public String getClientId() {
        return clientId;
    }

    public String getRedirectUri() {
        return redirectUri;
    }

    public String getAuthUrl() {
        return authUrl;
    }

    public String getTokenUrl() {
        return tokenUrl;
    }

    public String getInfoUrl() {
        return infoUrl;
    }

    public String getMessageUrl() {
        return messageUrl;
    }

    public int getConnectTimeoutMillis() {
        return connectTimeoutMillis;
    }

    public int getResponseTimeoutSeconds() {
        return responseTimeoutSeconds;
    }
}
