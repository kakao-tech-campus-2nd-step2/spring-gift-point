package gift.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "kakao")
public class KakaoProperties {

    private final String clientId;
    private final String redirectUrl;
    private final String authorizeUrl;
    private final String tokenUrl;
    private final String tokenInfoUrl;
    private final String userInfoUrl;
    private final String sendMessageForMe;

    public KakaoProperties(String clientId, String redirectUrl, String authorizeUrl,
        String tokenUrl,
        String tokenInfoUrl, String userInfoUrl, String sendMessageForMe) {
        this.clientId = clientId;
        this.redirectUrl = redirectUrl;
        this.authorizeUrl = authorizeUrl;
        this.tokenUrl = tokenUrl;
        this.tokenInfoUrl = tokenInfoUrl;
        this.userInfoUrl = userInfoUrl;
        this.sendMessageForMe = sendMessageForMe;
    }

    public String getClientId() {
        return clientId;
    }

    public String getRedirectUrl() {
        return redirectUrl;
    }

    public String getAuthorizeUrl() {
        return authorizeUrl;
    }

    public String getTokenUrl() {
        return tokenUrl;
    }

    public String getTokenInfoUrl() {
        return tokenInfoUrl;
    }

    public String getUserInfoUrl() {
        return userInfoUrl;
    }

    public String getSendMessageForMe() {
        return sendMessageForMe;
    }
}