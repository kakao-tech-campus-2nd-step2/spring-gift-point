package gift.auth;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "oauth.kakao")
public record KakaoOauthProperty(
        String redirectUri,
        String clientId,
        String[] scope,
        String adminKey
) {
}
