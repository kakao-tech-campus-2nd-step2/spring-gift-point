package gift.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "kakao")
public class KakaoProperties {

    private final String adminKey;
    private final String clientId;
    private final String redirectUri;
    private final String tokenRequestUri;
    private final String memberInfoRequestUri;
    private final String apiUrl;
    private final String loginUrl;
    private final String scope;
    private final String authBaseUrl;

    public KakaoProperties(String adminKey, String clientId, String redirectUri, String tokenRequestUri, String memberInfoRequestUri, String apiUrl, String loginUrl, String scope, String authBaseUrl) {
        this.adminKey = adminKey;
        this.clientId = clientId;
        this.redirectUri = redirectUri;
        this.tokenRequestUri = tokenRequestUri;
        this.memberInfoRequestUri = memberInfoRequestUri;
        this.apiUrl = apiUrl;
        this.loginUrl = loginUrl;
        this.scope = scope;
        this.authBaseUrl = authBaseUrl;
    }

    public String getAdminKey() {
        return adminKey;
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

    public String getApiUrl() {
        return apiUrl;
    }

    public String getLoginUrl() {
        return loginUrl;
    }

    public String getScope() {
        return scope;
    }

    public String getAuthBaseUrl() {
        return authBaseUrl;
    }
}
