package gift.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix="kakao")
public class KakaoProperties {
    private final String clientId;
    private final String redirectUri;
    private final String tokenRequestUri;
    private final String memberInfoRequestUri;

    public KakaoProperties(String clientId, String redirectUri, String tokenRequestUri, String memberInfoRequestUri) {
        this.clientId = clientId;
        this.redirectUri = redirectUri;
        this.tokenRequestUri = tokenRequestUri;
        this.memberInfoRequestUri = memberInfoRequestUri;
    }

    public String getClientId() {
        return clientId;
    }
    public String getRedirectUri() {
        return redirectUri;
    }
    public String getTokenRequestUri() {
        return tokenRequestUri;
    }
    public String getMemberInfoRequestUri() {
        return memberInfoRequestUri;
    }
}
