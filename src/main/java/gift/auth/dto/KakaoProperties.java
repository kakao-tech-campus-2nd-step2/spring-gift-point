package gift.auth.dto;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.util.LinkedMultiValueMap;

@ConfigurationProperties("kakao")
public record KakaoProperties(
        String clientId,
        String redirectUrl
) {
    public LinkedMultiValueMap<String, String> makeBody(String code) {
        LinkedMultiValueMap<String, String> body = new LinkedMultiValueMap<>();

        body.add("grant_type", "authorization_code");
        body.add("client_id", clientId());
        body.add("redirect_uri", redirectUrl());
        body.add("code", code);

        return body;
    }
}
