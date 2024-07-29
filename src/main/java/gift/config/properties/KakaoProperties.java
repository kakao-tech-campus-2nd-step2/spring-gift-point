package gift.config.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "kakao")
public record KakaoProperties(String restApiKey, String redirectUri, String tokenUri) {
}
