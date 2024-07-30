package gift.api;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "kakao")
public record KakaoProperties(String clientId,
                              String authorization,
                              String redirectUri,
                              String tokenUrl,
                              String memberUrl,
                              String messageUrl,
                              String tokenValidateUrl) {

    public String getAuthorizationUrl() {
        return authorization
            + "&redirect_uri=" + redirectUri
            + "&client_id=" + clientId;
    }
}
