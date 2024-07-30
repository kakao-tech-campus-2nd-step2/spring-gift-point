package gift.common.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;


@ConfigurationProperties(prefix = "kakao")
public record KakaoProperties(
    String clientId,
    String redirectUrl
) {

}
