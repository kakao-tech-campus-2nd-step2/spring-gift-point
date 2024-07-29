package gift.users.kakao;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "kakao")
public record KakaoProperties(String clientId, String redirectUri, String authUrl,
                              String tokenUrl, String userUrl, String loginResponseType,
                              String userHeaderValue, String tokenGrantType, String sendToMeUrl) {

}
