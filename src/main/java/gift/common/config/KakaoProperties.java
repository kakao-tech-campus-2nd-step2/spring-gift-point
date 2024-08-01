package gift.common.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "kakao.login")
public record KakaoProperties(
         String getAuthCodeUri,
         String getMessageToMeUri,
         String clientId,
         String redirectUri,
         String grantType) {
}
