package gift.service;

import gift.exception.KakaoAuthException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.util.Map;

@Service
public class KakaoAuthService {

    private static final Logger logger = LoggerFactory.getLogger(KakaoAuthService.class);

    private static final String GRANT_TYPE = "authorization_code";

    @Value("${kakao.client-id}")
    private String clientId;

    @Value("${kakao.redirect-uri}")
    private String redirectUri;

    @Value("${kakao.token-url}")
    private String tokenUrl;

    @Value("${kakao.user-info-url}")
    private String userInfoUrl;

    private final RestTemplate restTemplate;

    public KakaoAuthService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public String getAccessToken(String authorizationCode) {
        HttpHeaders headers = createHeaders();
        MultiValueMap<String, String> body = createRequestBody(authorizationCode);

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(body, headers);
        try {
            ResponseEntity<Map> response = restTemplate.exchange(URI.create(tokenUrl), HttpMethod.POST, request, Map.class);

            if (response.getStatusCode().is2xxSuccessful()) {
                Map<String, Object> responseBody = response.getBody();
                return (String) responseBody.get("access_token");
            } else {
                logger.error("Failed to get access token: " + response.getStatusCode() + " - " + response.getBody());
                throw new KakaoAuthException("Failed to get access token");
            }
        } catch (RestClientException e) {
            logger.error("Error while getting access token", e);
            throw new KakaoAuthException("Error while getting access token", e);
        }
    }

    private HttpHeaders createHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        return headers;
    }

    private MultiValueMap<String, String> createRequestBody(String authorizationCode) {
        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("grant_type", GRANT_TYPE);
        body.add("client_id", clientId);
        body.add("redirect_uri", redirectUri);
        body.add("code", authorizationCode);
        return body;
    }

    public String getUserEmail(String accessToken) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + accessToken);
        HttpEntity<?> entity = new HttpEntity<>(headers);

        try {
            ResponseEntity<Map> response = restTemplate.exchange(URI.create(userInfoUrl), HttpMethod.GET, entity, Map.class);

            if (response.getStatusCode().is2xxSuccessful()) {
                Map<String, Object> responseBody = response.getBody();
                Map<String, Object> kakaoAccount = (Map<String, Object>) responseBody.get("kakao_account");
                return (String) kakaoAccount.get("email");
            } else {
                logger.error("Failed to get user email: " + response.getStatusCode() + " - " + response.getBody());
                throw new KakaoAuthException("Failed to get user email");
            }
        } catch (RestClientException e) {
            logger.error("Error while getting user email", e);
            throw new KakaoAuthException("Error while getting user email", e);
        }
    }
}
