package gift.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@ConfigurationPropertiesScan
@ConfigurationProperties("kakao")
public record KakaoProperties(
    String clientId,
    String redirectUrl,
    String authUrl,
    String tokenUrl,
    String userInfoUrl,
    String sendMessageUrl
) {

    public String generateLoginUrl() {
        return String.format("%s?response_type=code&client_id=%s&redirect_uri=%s",
            authUrl, clientId, redirectUrl);
    }
}