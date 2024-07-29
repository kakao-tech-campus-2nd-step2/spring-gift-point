package gift.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.util.Map;

@Service
public class KakaoLoginService {

    @Value("${kakao.client-id}")
    private String clientId;

    @Value("${kakao.redirect-uri}")
    private String redirectUri;

    public String getAccessToken(String authorizationCode) throws Exception {
        String url = "https://kauth.kakao.com/oauth/token";
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_FORM_URLENCODED_VALUE);

        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("grant_type", "authorization_code");
        body.add("client_id", clientId);
        body.add("redirect_uri", redirectUri);
        body.add("code", authorizationCode);

        RequestEntity<MultiValueMap<String, String>> request = new RequestEntity<>(body, headers, HttpMethod.POST, URI.create(url));

        RestTemplate restTemplate = new RestTemplate();
        try {
            ResponseEntity<Map> response = restTemplate.exchange(request, Map.class);
            if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
                System.out.println("Response Body: " + response.getBody());
                System.out.println((String) response.getBody().get("access_token"));
                return (String) response.getBody().get("access_token");
            } else {
                throw new Exception("Failed to get access token from Kakao");
            }
        } catch (RestClientException e) {
            throw new Exception("Error while requesting access token from Kakao", e);
        }
    }

    public Map<String, Object> getUserInfo(String accessToken) throws Exception {
        String url = "https://kapi.kakao.com/v2/user/me";
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken);

        RequestEntity<Void> request = new RequestEntity<>(headers, HttpMethod.GET, URI.create(url));

        RestTemplate restTemplate = new RestTemplate();
        try {
            ResponseEntity<Map> response = restTemplate.exchange(request, Map.class);
            if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
                return response.getBody();
            } else {
                throw new Exception("Failed to get user info from Kakao");
            }
        } catch (RestClientException e) {
            throw new Exception("Error while requesting user info from Kakao", e);
        }
    }
}
