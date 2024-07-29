package gift.oauth.presentation.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("spring.oauth.kakao")
public record KakaoConfig(
    String clientId,
    String redirectUri,
    String grantType,
    String codeUrl
) {

}
