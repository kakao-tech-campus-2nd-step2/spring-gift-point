package gift.kakaoApi.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "kakao")
public record KakaoProperties(
    String clientId,
    String redirectUrl,
    String authorizationCodeUrl,
    String tokenUrl,
    String accountUrl,
    String sendMessageUrl
) {

}
