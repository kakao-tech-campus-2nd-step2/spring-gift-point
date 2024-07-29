package gift.oauth;

import gift.oauth.response.KakaoInfoResponse;
import gift.oauth.response.KakaoTokenResponse;
import java.net.URI;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.client.RestClient;

@Service
public class KakaoOAuthService {

    private final KakaoApiSecurityProperties kakaoApiSecurityProps;
    private final RestClient client;

    public KakaoOAuthService(KakaoApiSecurityProperties kakaoApiSecurityProps, RestClient client) {
        this.kakaoApiSecurityProps = kakaoApiSecurityProps;
        this.client = client;
    }

    public URI getKakaoLoginPage() {
        return kakaoApiSecurityProps.getLoginUri();
    }

    public KakaoTokenResponse requestToken(String code) {
        var uri = kakaoApiSecurityProps.getTokenUri();
        var body = getTokenRequestBody(code);
        var response = client.post().uri(uri)
            .contentType(MediaType.APPLICATION_FORM_URLENCODED).body(body)
            .retrieve()
            .toEntity(KakaoTokenResponse.class);
        return response.getBody();
    }

    public Long getKakaoId(String token) {
        var uri = kakaoApiSecurityProps.getUserInfoUri();
        var response = client.post().uri(uri).contentType(MediaType.APPLICATION_FORM_URLENCODED)
            .headers(httpHeaders -> httpHeaders.setBearerAuth(token))
            .retrieve().toEntity(KakaoInfoResponse.class);
        return response.getBody().id();
    }

    public LinkedMultiValueMap<String, String> getTokenRequestBody(String code) {
        LinkedMultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("grant_type", "authorization_code");
        body.add("client_id", kakaoApiSecurityProps.getClientId());
        body.add("redirect_uri", kakaoApiSecurityProps.getRedirectUri());
        body.add("code", code);
        return body;
    }

}
