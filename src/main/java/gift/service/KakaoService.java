package gift.service;

import gift.dto.KakaoAccessToken;
import gift.dto.KakaoProperties;
import gift.dto.KakaoUserInfo;
import java.net.URI;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;

@Service
public class KakaoService {

    private final KakaoProperties kakaoProperties;
    private final KakaoApiClient kakaoApiClient;
    private final RestTemplate restTemplate = new RestTemplate();

    private final static String KAKAO_AUTH_URI = "https://kauth.kakao.com";

    public KakaoService(KakaoProperties kakaoProperties, KakaoApiClient kakaoApiClient) {
        this.kakaoProperties = kakaoProperties;
        this.kakaoApiClient = kakaoApiClient;
    }

    public String getKakaoLogin() {
        return KAKAO_AUTH_URI + "/oauth/authorize" + "?scope=talk_message" + "&response_type=code"
            + "&redirect_uri=" + kakaoProperties.getRedirectUrl()
            + "&client_id=" + kakaoProperties.getClientId();
    }
    // https://kauth.kakao.com/oauth/authorize?scope=talk_message&response_type=code
    // &redirect_uri={kakaoRedirectUrl}&client_id={kakaoClientId}

    public KakaoAccessToken getAccessToken(String code) {
        String url = KAKAO_AUTH_URI + "/oauth/token";
        HttpHeaders headers = createHeaders();
        MultiValueMap<String, String> body = createBody(code);

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(body, headers);
        try {
            ResponseEntity<KakaoAccessToken> response = restTemplate.exchange(URI.create(url),
                HttpMethod.POST,
                request, KakaoAccessToken.class);

            System.out.println("Response: " + response);

            return extractAccessToken(response);
        } catch (RestClientException e) {
            throw new RuntimeException("Error while getting access token", e);
        }

    }

    public KakaoUserInfo getUserInfo(String accessToken) {
        return kakaoApiClient.getUserInfo(accessToken);
    }

    private HttpHeaders createHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_FORM_URLENCODED_VALUE);
        return headers;
    }

    private MultiValueMap<String, String> createBody(String code) {
        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("grant_type", "authorization_code");
        body.add("client_id", kakaoProperties.getClientId());
        body.add("redirect_uri", kakaoProperties.getRedirectUrl());
        body.add("code", code);
        return body;
    }

    private KakaoAccessToken extractAccessToken(ResponseEntity<KakaoAccessToken> response) {
        if (response.getStatusCode() != HttpStatus.OK) {
            throw new IllegalArgumentException(
                "access token 발급 실패 : HTTP status " + response.getStatusCode());
        }

        KakaoAccessToken responseBody = response.getBody();
        if (responseBody == null || responseBody.getAccessToken() == null) {
            throw new IllegalArgumentException("No access token was found in the response.");
        }

        return responseBody;
    }

}
