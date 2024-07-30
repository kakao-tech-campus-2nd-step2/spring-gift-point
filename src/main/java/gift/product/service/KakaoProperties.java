package gift.product.service;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("kakao")
public record KakaoProperties(
    String clientId,
    String redirectUrl
) {
}
