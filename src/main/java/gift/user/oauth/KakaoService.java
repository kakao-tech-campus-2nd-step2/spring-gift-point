package gift.user.oauth;

import java.net.URI;
import java.util.Map;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.client.RestClient;

@Service
public class KakaoService {

    private final RestClient client = RestClient.builder().build();
    private final KakaoProperties kakaoProperties;

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

    // 현재는 닉네임 권한만 허용이 되는 상태라, 유저 정보를 닉네임으로 한정지었습니다.
    public String getUserInfo(String accessToken) {
        Map response = getInfoResponse(accessToken);
        Map<String, Object> kakaoAccount = (Map<String, Object>) response.get("kakao_account");
        Map<String, Object> profile = (Map<String, Object>) kakaoAccount.get("profile");
        return (String) profile.get("nickname");
    }

    private Map getTokenResponse(String code) {
        String url = "https://kauth.kakao.com/oauth/token";
        LinkedMultiValueMap<String, String> body = buildTokenRequest(code);
        Map response = client.post()
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

    private Map getInfoResponse(String accessToken) {
        String url = "https://kapi.kakao.com/v2/user/me";
        String header = "Bearer " + accessToken;

        Map response = client.get()
                .uri(URI.create(url))
                .header("Authorization", header)
                .retrieve()
                .toEntity(Map.class)
                .getBody();

        return response;
    }

}