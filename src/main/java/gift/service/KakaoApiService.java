package gift.service;

import gift.model.KakaoTokenDTO;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

@Service
public class KakaoApiService {

    private final RestTemplate restTemplate;
    @Value("${kakao.client-id}")
    private String clientId;
    @Value("${kakao.redirect-uri}")
    private String redirectUri;

    public KakaoApiService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public String getAccessToken(String code) {
        String url = "https://kauth.kakao.com/oauth/token";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("grant_type", "authorization_code");
        params.add("client_id", clientId);
        params.add("redirect_uri", redirectUri);
        params.add("code", code);

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(params, headers);

        ResponseEntity<KakaoTokenDTO> response = restTemplate.exchange(
            URI.create(url),
            HttpMethod.POST,
            request,
            KakaoTokenDTO.class
        );

        KakaoTokenDTO responseBody = response.getBody();
        if (responseBody == null) {
            return null;
        }
        return responseBody.access_token();
    }

    public String getKakaoProfileNickname(String accessToken) {
        String url = "https://kapi.kakao.com/v2/user/me";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        headers.add(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken);

        HttpEntity<?> request = new HttpEntity<>(headers);

        ResponseEntity<HashMap> response = restTemplate.exchange(
            URI.create(url),
            HttpMethod.GET,
            request,
            HashMap.class
        );

        if (response != null && response.getBody() != null) {
            Map<String, Object> responseBody = response.getBody();
            Map<String, Object> kakaoAccount = (Map<String, Object>) responseBody.get(
                "kakao_account");
            Map<String, Object> profile = (Map<String, Object>) kakaoAccount.get("profile");
            return (String) profile.get("nickname");
        }
        return null;
    }
}