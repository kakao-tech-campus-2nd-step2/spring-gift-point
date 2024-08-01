package gift.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "kakao")
public record KakaoProperties(
        String grant_type,
        String clientId,
        String redirectURL
) {
}
