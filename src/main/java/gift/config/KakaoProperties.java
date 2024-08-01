package gift.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class KakaoProperties {

    @Value("${kakao.client-id}")
    private String clientId;

    @Value("${kakao.redirect-uri}")
    private String redirectUri;

    @Value("${kakao.client-secret}")
    private String clientSecret;

    @Value("${kakao.api-url}")
    private String kakaoApiUrl;

    @Value("${kakao.admin-key}")
    private String adminKey;

    public String getClientId() {
        return clientId;
    }

    public String getRedirectUri() {
        return redirectUri;
    }

    public String getClientSecret() {
        return clientSecret;
    }

    public String getKakaoApiUrl() {
        return kakaoApiUrl;
    }

    public String getAdminKey() {
        return adminKey;
    }
}
