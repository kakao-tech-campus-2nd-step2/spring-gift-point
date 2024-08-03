package gift.kakaomember.property;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "kakao")
public record KakaoProperties(
    String clientId,
    String redirectUrl
) { }
