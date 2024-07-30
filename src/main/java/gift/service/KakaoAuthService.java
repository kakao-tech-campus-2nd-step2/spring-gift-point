package gift.service;

import gift.dto.response.KakaoProfileResponse;
import gift.dto.response.KakaoTokenResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.net.URI;

@Service
public class KakaoAuthService {

    private static final String GRANT_TYPE = "authorization_code";
    private static final String TOKEN_URL_SUFFIX = "/oauth/token";
    private static final String PROFILE_URL_SUFFIX = "/v2/user/me";

    @Value("${kakao.client-id}")
    private String clientId;

    @Value("${kakao.redirect-uri}")
    private String redirectUri;

    @Value("${kakao.api.url}")
    private String kakaoApiUrl;

    private final RestTemplate restTemplate;

    @Autowired
    public KakaoAuthService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public KakaoTokenResponse getKakaoToken(String authorizationCode) {
        String url = kakaoApiUrl + TOKEN_URL_SUFFIX;

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_FORM_URLENCODED_VALUE);

        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("grant_type", GRANT_TYPE);
        body.add("client_id", clientId);
        body.add("redirect_uri", redirectUri);
        body.add("code", authorizationCode);

        RequestEntity<MultiValueMap<String, String>> request = new RequestEntity<>(body, headers, HttpMethod.POST, URI.create(url));

        try {
            ResponseEntity<KakaoTokenResponse> response = restTemplate.exchange(
                    request,
                    KakaoTokenResponse.class
            );
            validateResponse(response);

            return response.getBody();
        } catch (HttpClientErrorException e) {
            throw new RuntimeException("Access token 얻기 실패: "  + e.getStatusCode() + " - " + e.getResponseBodyAsString(), e);
        } catch (Exception e) {
            throw new RuntimeException("Access token을 얻는 중 예상치 못한 오류가 발생했습니다", e);
        }
    }

    public KakaoProfileResponse getUserProfile(String accessToken) {
        String url = kakaoApiUrl + PROFILE_URL_SUFFIX;

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken);
        headers.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);

        HttpEntity<Void> request = new HttpEntity<>(headers);

        try {
            ResponseEntity<KakaoProfileResponse> response = restTemplate.exchange(
                    url,
                    HttpMethod.GET,
                    request,
                    KakaoProfileResponse.class
            );
            validateResponse(response);

            return response.getBody();
        } catch (HttpClientErrorException e) {
            throw new RuntimeException("사용자 정보 얻기 실패: " + e.getStatusCode() + " - " + e.getResponseBodyAsString(), e);
        } catch (Exception e) {
            throw new RuntimeException("사용자 정보를 얻는 중 예상치 못한 오류가 발생했습니다", e);
        }
    }

    private <T> void validateResponse(ResponseEntity<T> response) {
        if (!response.getStatusCode().is2xxSuccessful()) {
            throw new RuntimeException("HTTP 상태 " + response.getStatusCode());
        }
        if (response.getBody() == null){
            throw new RuntimeException("응답 바디가 null입니다.");
        }
    }

}
