package gift.Util;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "kakao")
public record KakaoProperties(
        String clientId,
        String redirectUrl,
        String messageToMeUri,
        String messageToMeWebUri,
        String generateTokenUri,
        String loginUri,
        String getUserInfoUri
) {
}
