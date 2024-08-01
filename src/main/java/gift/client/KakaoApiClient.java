package gift.client;

import gift.config.KakaoProperties;
import gift.dto.KakaoTokenResponse;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.net.URISyntaxException;

@Component
public class KakaoApiClient {

    private final RestTemplate restTemplate;
    private final KakaoProperties kakaoProperties;

    public KakaoApiClient(RestTemplate restTemplate, KakaoProperties kakaoProperties) {
        this.restTemplate = restTemplate;
        this.kakaoProperties = kakaoProperties;
    }

    public KakaoTokenResponse getAccessToken(String code) {
        try {
            String tokenUri = "https://kauth.kakao.com/oauth/token";
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

            MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
            body.add("grant_type", "authorization_code");
            body.add("client_id", kakaoProperties.getClientId());
            body.add("redirect_uri", kakaoProperties.getRedirectUri());
            body.add("code", code);
            body.add("client_secret", kakaoProperties.getClientSecret());

            HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(body, headers);
            ResponseEntity<KakaoTokenResponse> response = restTemplate.exchange(new URI(tokenUri), HttpMethod.POST, request, KakaoTokenResponse.class);
            return response.getBody();
        } catch (URISyntaxException e) {
            throw new RuntimeException("Failed to get access token", e);
        }
    }

    public ResponseEntity<String> getForEntity(URI uri, HttpHeaders headers) {
        HttpEntity<String> request = new HttpEntity<>(headers);
        return restTemplate.exchange(uri, HttpMethod.GET, request, String.class);
    }
}
