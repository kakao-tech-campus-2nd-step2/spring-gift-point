package gift.oauth;

import com.google.gson.Gson;
import java.net.URI;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.client.RestClient;

@Service
public class OauthService {

    private final KakaoOAuthConfigProperties configProperties;
    private final RestClient client;

    public OauthService(KakaoOAuthConfigProperties configProperties, RestClient client) {
        this.configProperties = configProperties;
        this.client = client;
    }

    public LinkedMultiValueMap<String, String> getRequestBody(String code) {
        var body = new LinkedMultiValueMap<String, String>();
        body.add("grant_type", "authorization_code");
        body.add("client_id", configProperties.getClientId());
        body.add("redirect_uri", configProperties.getRedirectUrl());
        body.add("code", code);

        return body;
    }

    public KakaoToken getKakaoToken(String code) {
        String url = "https://kauth.kakao.com/oauth/token";

        var body = getRequestBody(code);

        var response = client.post()
            .uri(URI.create(url))
            .contentType(MediaType.APPLICATION_FORM_URLENCODED)
            .body(body) // request body
            .retrieve()
            .toEntity(String.class);

        return extractKakaoToken(response.getBody());
    }

    public KakaoToken extractKakaoToken(String responseBody) {
        Gson gson = new Gson();
        return gson.fromJson(responseBody, KakaoToken.class);
    }
}
