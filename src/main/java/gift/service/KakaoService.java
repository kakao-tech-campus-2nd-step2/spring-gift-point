package gift.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientResponseException;

import java.util.Map;

@Service
public class KakaoService {

    @Value("${kakao.client-id}")
    private String kakaoClientId;

    @Value("${kakao.redirect-url}")
    private String kakaoRedirectUri;

    private final RestClient restClient;

    public KakaoService(RestClient restClient) {
        this.restClient = restClient;
    }

    public String getAccessToken(String code) {
        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("grant_type", "authorization_code");
        body.add("client_id", kakaoClientId);
        body.add("redirect_uri", kakaoRedirectUri);
        body.add("code", code);

        try {
            String response = restClient
                    .post()
                    .uri("https://kauth.kakao.com/oauth/token")
                    .body(body)
                    .retrieve()
                    .body(String.class);

            ObjectMapper objectMapper = new ObjectMapper();
            Map<String, Object> tokenResponse = objectMapper.readValue(response, Map.class);
            return (String) tokenResponse.get("access_token");
        } catch (RestClientResponseException | JsonProcessingException e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to retrieve Kakao token", e);
        }
    }

    public Map<String, Object> getUserInfo(String accessToken) {
        try {
            String userInfoResponse = restClient
                    .get()
                    .uri("https://kapi.kakao.com/v2/user/me")
                    .header("Authorization", "Bearer " + accessToken)
                    .retrieve()
                    .body(String.class);

            ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.readValue(userInfoResponse, Map.class);
        } catch (RestClientResponseException | JsonProcessingException e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to retrieve Kakao user info", e);
        }
    }
}
