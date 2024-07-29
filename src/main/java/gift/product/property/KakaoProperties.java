package gift.product.property;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "kakao")
public record KakaoProperties(
    String grantType,
    String clientId,
    String redirectUrl,
    String clientSecret
) {

}
