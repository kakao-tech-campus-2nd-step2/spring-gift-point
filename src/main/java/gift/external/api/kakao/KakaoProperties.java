package gift.external.api.kakao;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "kakao")
public record KakaoProperties(
    String clientId,
    String redirectUri,
    String authBaseUrl,
    String apiBaseUrl
) {

}
