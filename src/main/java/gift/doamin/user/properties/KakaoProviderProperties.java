package gift.doamin.user.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "my.oauth2.provider.kakao")
public record KakaoProviderProperties(String authorizationUri, String tokenUri,
                                      String userInfoUri) {

}
