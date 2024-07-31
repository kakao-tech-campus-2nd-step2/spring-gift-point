package gift.kakao.vo;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.util.LinkedMultiValueMap;

@ConfigurationProperties("kakao")
public record KakaoProperties(
        String authGrantType,
        String refreshGrantType,
        String redirectUri,
        String clientId,
        String authorizationPrefix,
        String authDomainName,
        String apiDomainName,
        String baseDomainName
) {
    public LinkedMultiValueMap<String, String> getAuthRequestBody(String code) {
        LinkedMultiValueMap<String, String> requestBody = new LinkedMultiValueMap<>();
        requestBody.add("grant_type", this.authGrantType);
        requestBody.add("client_id", this.clientId);
        requestBody.add("redirect_uri", this.redirectUri);
        requestBody.add("code", code);
        return requestBody;
    }

    public LinkedMultiValueMap<String, String> getRefreshRequestBody(String refreshToken) {
        LinkedMultiValueMap<String, String> requestBody = new LinkedMultiValueMap<>();
        requestBody.add("grant_type", this.refreshGrantType);
        requestBody.add("client_id", this.clientId);
        requestBody.add("refresh_token", refreshToken);
        return requestBody;
    }

    public String getKakaoAuthUrl() {
        return "https://kauth.kakao.com/oauth/authorize?response_type=code" +
                "&redirect_uri=" + this.redirectUri +
                "&client_id="    + this.clientId;
    }

}
