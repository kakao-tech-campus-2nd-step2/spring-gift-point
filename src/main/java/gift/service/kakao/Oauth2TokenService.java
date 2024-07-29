package gift.service.kakao;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import gift.domain.member.SocialAccount;
import gift.exception.ErrorCode;
import gift.exception.GiftException;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.client.RestTemplate;

import static org.springframework.http.MediaType.APPLICATION_FORM_URLENCODED;

@Service
public class Oauth2TokenService {

    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;
    private final KakaoProperties kakaoProperties;

    public Oauth2TokenService(RestTemplate restTemplate, ObjectMapper objectMapper, KakaoProperties kakaoProperties) {
        this.restTemplate = restTemplate;
        this.objectMapper = objectMapper;
        this.kakaoProperties = kakaoProperties;
    }

    @Transactional
    public void refreshAccessToken(SocialAccount socialAccount) {
        HttpEntity<LinkedMultiValueMap<String, String>> request = createTokenRequest(socialAccount.getRefreshToken());
        ResponseEntity<String> response = sendTokenRequest(request);

        if (response.getStatusCode().is2xxSuccessful()) {
            handleTokenResponse(response.getBody(), socialAccount);
        } else {
            throw new GiftException(ErrorCode.REFRESH_TOKEN_FAILED);
        }
    }

    public boolean isAccessTokenExpired(String accessToken) {
        ResponseEntity<String> response = requestAccessTokenInfo(accessToken);
        if (response.getStatusCode() == HttpStatus.UNAUTHORIZED) {
            return true;
        }

        int expiresIn = parseExpiresIn(response.getBody());
        return expiresIn <= 0;
    }

    private HttpEntity<LinkedMultiValueMap<String, String>> createTokenRequest(String refreshToken) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(APPLICATION_FORM_URLENCODED);

        LinkedMultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("grant_type", "refresh_token");
        body.add("client_id", kakaoProperties.clientId());
        body.add("refresh_token", refreshToken);

        return new HttpEntity<>(body, headers);
    }

    private ResponseEntity<String> sendTokenRequest(HttpEntity<LinkedMultiValueMap<String, String>> request) {
        String url = "https://kauth.kakao.com/oauth/token";
        return restTemplate.postForEntity(url, request, String.class);
    }

    private void handleTokenResponse(String responseBody, SocialAccount socialAccount) {
        try {
            JsonNode root = objectMapper.readTree(responseBody);
            String newAccessToken = root.path("access_token").asText();
            String newRefreshToken = root.path("refresh_token").asText();

            socialAccount.changeAccessToken(newAccessToken);
            if (!newRefreshToken.isEmpty()) {
                socialAccount.changeRefreshToken(newRefreshToken);
            }
        } catch (JsonProcessingException e) {
            throw new GiftException(ErrorCode.JSON_PROCESSING_FAILED);
        }
    }

    private ResponseEntity<String> requestAccessTokenInfo(String accessToken) {
        String url = "https://kapi.kakao.com/v1/user/access_token_info";
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(accessToken);

        HttpEntity<Void> httpEntity = new HttpEntity<>(headers);
        return restTemplate.exchange(url, HttpMethod.GET, httpEntity, String.class);
    }

    private int parseExpiresIn(String responseBody) {
        try {
            JsonNode responseBodyJson = objectMapper.readTree(responseBody);
            return responseBodyJson.path("expires_in").asInt();
        } catch (JsonProcessingException e) {
            throw new GiftException(ErrorCode.JSON_PROCESSING_FAILED);
        }
    }

}
