package gift.auth.dto;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.util.LinkedMultiValueMap;


@ConfigurationProperties(prefix = "kakao")
public record KakaoProperties(String clientId, String redirectUrl) {
    public LinkedMultiValueMap<String, String> toBody(String code) {
        LinkedMultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("grant_type", "authorization_code");
        body.add("client_id", this.clientId);
        body.add("redirect_uri", this.redirectUrl);
        body.add("code", code);
        return body;
    }
}
