package gift.kakao;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("kakao")
public record KakaoProperties(
    String clientId,
    String redirectUri
) {

}
