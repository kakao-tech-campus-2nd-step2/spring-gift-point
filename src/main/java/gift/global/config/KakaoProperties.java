package gift.global.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "kakao")
public record KakaoProperties(
    String grantType,
    String messageRequestUri,
    String tokenRequestUri,
    String userRequestUri,
    String redirectUri,
    String clientId,
    String refreshGrantType
) {

    public String getKakaoLoginUrl() {
        var responseType = "code";
        return "https://kauth.kakao.com/oauth/authorize?client_id=" + clientId + "&response_type="
            + responseType + "&redirect_uri=" + redirectUri;
    }
}