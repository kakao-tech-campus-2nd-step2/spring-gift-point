package gift.common.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("kakao")
public record KakaoProperties(
    String redirectUri,
    String clientId,
    String authUrl,
    String tokenUrl,
    String userInfoUrl,
    String webUrl,
    String messageUrl,
    String refreshUrl
) {

}
