package gift.services;

import gift.classes.Exceptions.AuthException;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

@Service
public class KaKaoService {

    @Value("${kakao.client.id}")
    private String KAKAO_CLIENT_ID;

    @Value("${kakao.redirect.url}")
    private String KAKAO_REDIRECT_URL;

    @Value("${kakao.auth.uri}")
    private String KAKAO_AUTH_URI;

    @Value("${kakao.api.uri}")
    private String KAKAO_API_URI;

    public String getKaKaoLogin() {
        return KAKAO_AUTH_URI
            + "?client_id=" + KAKAO_CLIENT_ID
            + "&redirect_uri=" + KAKAO_REDIRECT_URL
            + "&response_type=code";
    }

    public String getKaKaoToken(String code) {
        if (code == null) {
            throw new AuthException("Failed to get authorization code.");
        }

        String accessToken = "";

        try {
            HttpHeaders headers = new HttpHeaders();
            headers.add("Content-type", "application/x-www-form-urlencoded");

            MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
            body.add("grant_type", "authorization_code");
            body.add("client_id", KAKAO_CLIENT_ID);
            body.add("redirect_uri", KAKAO_REDIRECT_URL);
            body.add("code", code);

            RestTemplate restTemplate = new RestTemplate();
            HttpEntity<MultiValueMap<String, String>> httpEntity = new HttpEntity<>(body, headers);

            ResponseEntity<String> response = restTemplate.exchange(
                KAKAO_AUTH_URI,
                HttpMethod.POST,
                httpEntity,
                String.class
            );

            JSONParser jsonParser = new JSONParser();
            JSONObject jsonObj = (JSONObject) jsonParser.parse(response.getBody());

            accessToken = (String) jsonObj.get("access_token");

        } catch (Exception e) {
            throw new AuthException("API call failed");
        }

        return accessToken;
    }

}
