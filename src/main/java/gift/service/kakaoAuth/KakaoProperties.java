package gift.service.kakaoAuth;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.util.LinkedMultiValueMap;

@ConfigurationProperties(prefix = "kakao")
public record KakaoProperties(
    String clientId,
    String redirectUri,
    String tokenPostUrl,
    String memberInfoPostUrl,
    String sendMessageTemplateUrl,
    String authSettingUrl
) {

    public LinkedMultiValueMap<String, String> createBody(String code) {

        if(code == null) {
            throw new IllegalArgumentException("code cannot be null");
        }

        LinkedMultiValueMap<String, String> body = new LinkedMultiValueMap<>();

        body.add("grant_type", "authorization_code");
        body.add("client_id", clientId);
        body.add("redirect_uri", redirectUri);
        body.add("code", code);
        return body;
    }
}
