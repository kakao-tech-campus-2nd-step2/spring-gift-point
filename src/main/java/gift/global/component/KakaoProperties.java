package gift.global.component;


import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "auth.kakao")
public record KakaoProperties(
    String clientId,
    String redirectUri,
    String responseType,
    String grantType
) {

}
