package gift.kakao_login;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "kakao.login")
public record KakaoProperties(String key, String authorizationCode, String clientId, String redirectUri, String grantType) {
}
