package gift.dto;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "kakao")
public class KakaoProperties {

    @Value("${kakao.client-id}")
    private String clientId;

    @Value("${kakao.redirect-uri}")
    private String redirectUri;

    @Value("${kakao.auth-url}")
    private String authUrl;

    @Value("${kakao.api-key}")
    private String apiKey;

    @Value("${kakao.api-url}")
    private String apiUrl;

    @Value("${kakao.message-url}")
    private String messageUrl;

    @Value("${kakao.access-token-info-url}")
    private String accessTokenInfoUrl;

    public String getClientId() {
        return clientId;
    }

    public String getRedirectUri() {
        return redirectUri;
    }

    public String getAuthUrl() {
        return authUrl;
    }

    public String getApiKey() {
        return apiKey;
    }

    public String getApiUrl() {
        return apiUrl;
    }

    public String getMessageUrl() {
        return messageUrl;
    }

    public String getAccessTokenInfoUrl() {
        return accessTokenInfoUrl;
    }
}