package gift.service;

import com.fasterxml.jackson.core.io.JsonEOFException;
import gift.domain.KakaoTokenResponseDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.configurationprocessor.json.JSONArray;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.client.RestClient;
import org.springframework.http.HttpStatusCode;

import java.net.URI;

@Service
public class KakaoLoginService {
    private String clientId;
    private String redirectUri;
    private String clientSecret;

    private final RestClient client = RestClient.builder().build();

    public KakaoLoginService(@Value("${kakao.client_id}") String clientId,
                             @Value("${kakao.redirect_url}") String redirectUri,
                             @Value("${kakao.client_secret}") String clientSecret) {
        this.clientId = clientId;
        this.redirectUri = redirectUri;
        this.clientSecret = clientSecret;
    }

    public String getToken(String code) {
        var body = new LinkedMultiValueMap<String, String>();
        body.add("grant_type", "authorization_code");
        body.add("client_id", clientId);
        body.add("redirect_uri", redirectUri);
        body.add("code", code);
        body.add("client_secret", clientSecret);

        return request("https://kauth.kakao.com/oauth/token", body, null).accessToken;
    }

    public String getEmail(String token) {
        var body = new LinkedMultiValueMap<String, JSONArray>();
        var property_keys = new JSONArray();
        property_keys.put("kakao_account.email");
        body.add("property_keys", property_keys);

        return request("https://kapi.kakao.com/v2/user/me", body, token).getEmail();
    }

    public void sendMessage(String token, String message) {
        var body = new LinkedMultiValueMap<String, String>();
        body.add("object_type", "text");
        body.add("text", message);

        request("https://kapi.kakao.com/v2/api/talk/memo/default/send", body, token);
    }

    public KakaoTokenResponseDTO request(String uri, LinkedMultiValueMap body, String token) {
        var requestBuilder  = client.post()
                .uri(URI.create(uri))
                .header("Content-type", "application/x-www-form-urlencoded;charset=utf-8")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(body);

        if(token != null) {
            requestBuilder.header("Authorization", "Bearer " + token);
        }

        return requestBuilder
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError, (request, response) -> {
                    throw new RuntimeException("잘못된 토큰 요청입니다."+ body);
                })
                .onStatus(HttpStatusCode::is5xxServerError, (request, response) -> {
                    throw new RuntimeException("카카오 서버 오류입니다.");
                })
                .body(KakaoTokenResponseDTO.class);
    }
}