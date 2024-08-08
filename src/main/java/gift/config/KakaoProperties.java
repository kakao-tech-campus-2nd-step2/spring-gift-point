package gift.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@Setter
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

}
