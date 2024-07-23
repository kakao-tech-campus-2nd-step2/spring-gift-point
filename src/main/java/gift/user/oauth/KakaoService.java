package gift.user.oauth;

import java.net.URI;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.client.RestClient;

@Service
public class KakaoService {

    private final RestClient client = RestClient.builder().build();
    private final KakaoProperties kakaoProperties;

    @Autowired
    public KakaoService(KakaoProperties kakaoProperties) {
        this.kakaoProperties = kakaoProperties;
    }

    public String buildAuthorizeUrl() {
        return "https://kauth.kakao.com/oauth/authorize?" +
                "client_id=" + kakaoProperties.clientId() +
                "&redirect_uri=" + kakaoProperties.redirectUri() +
                "&response_type=code";
    }

    public KakaoAuthToken getAccessToken(String code) {
        Map response = getTokenResponse(code);
        String accessToken = (String) response.get("access_token");
        String refreshToken = (String) response.get("refresh_token");
        return new KakaoAuthToken(accessToken, refreshToken);
    }

    private Map getTokenResponse(String code) {
        var url = "https://kauth.kakao.com/oauth/token";
        var body = buildTokenRequest(code);
        var response = client.post()
                .uri(URI.create(url))
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(body)
                .retrieve()
                .toEntity(Map.class)
                .getBody();

        return response;
    }

    private LinkedMultiValueMap<String, String> buildTokenRequest(String code) {
        LinkedMultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("grant_type", "authorization_code");
        body.add("client_id", kakaoProperties.clientId());
        body.add("redirect_uri", kakaoProperties.redirectUri());
        body.add("code", code);
        return body;
    }


}