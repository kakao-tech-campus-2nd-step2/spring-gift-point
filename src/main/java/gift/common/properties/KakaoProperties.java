package gift.common.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;


@ConfigurationProperties(prefix = "kakao")
public record KakaoProperties(
        String redirectUri,
        String restAPiKey,
        String tokenUrl,
        String authUrl,
        String userInfoUrl,
        String sendMessageUrl
) {
}