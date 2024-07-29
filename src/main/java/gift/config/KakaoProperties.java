package gift.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("kakao")
public record KakaoProperties(String clientId,
                              String redirectUrl,
                              String loginUrl,
                              String tokenUrl,
                              String userInfoUrl,
                              String messageUrl) { }
