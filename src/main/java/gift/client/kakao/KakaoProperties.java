package gift.client.kakao;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("kakao")
public record KakaoProperties(
    String clientId,
    String redirectUri,
    String tokenUrl,
    String userInfoUrl,
    String messageUrl,
    String templateId
) {

}
