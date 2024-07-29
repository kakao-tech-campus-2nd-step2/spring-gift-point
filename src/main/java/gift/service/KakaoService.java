package gift.service;

import gift.dto.response.KaKaoMemberInfoResponse;
import gift.dto.response.KakaoTokenResponse;
import gift.exception.customException.MissingAuthorizationCodeException;
import gift.exception.customException.ResponseBodyNullException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.util.Optional;

import static gift.exception.errorMessage.Messages.*;

@Service
public class KakaoService {

    @Value("${kakao.client-id}")
    private String clientId;
    @Value("${kakao.redirect-url}")
    private String redirectUri;
    private final RestTemplate restTemplate;

    public KakaoService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public String getKakaoCodeUrl() {
        String baseUrl = "https://kauth.kakao.com/oauth/authorize";
        return baseUrl + "?response_type=code&client_id=" + clientId + "&redirect_uri=" + redirectUri;
    }

    public String getKakaoToken(String code) {
        if (code == null || code.isEmpty()) {
            throw new MissingAuthorizationCodeException(MISSING_AUTHORIZATION_CODE);
        }

        var url = "https://kauth.kakao.com/oauth/token";
        var headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_FORM_URLENCODED_VALUE);

        var body = new LinkedMultiValueMap<String, String>();
        body.add("grant_type", "authorization_code");
        body.add("client_id", clientId);
        body.add("redirect_uri", redirectUri);
        body.add("code", code);
        var request = new RequestEntity<>(body, headers, HttpMethod.POST, URI.create(url));
        ResponseEntity<KakaoTokenResponse> response = restTemplate.exchange(request, KakaoTokenResponse.class);
        KakaoTokenResponse kakaoTokenResponse = Optional.ofNullable(response.getBody()).orElseThrow(() -> new ResponseBodyNullException(RESPONSE_BODY_NULL));

        System.out.println("accessToken: "+kakaoTokenResponse.access_token());
        return kakaoTokenResponse.access_token();
    }

    public String getKakaoUserEmail(String token) {
        var url = "https://kapi.kakao.com/v2/user/me";

        var headers = new HttpHeaders();
        headers.add(HttpHeaders.AUTHORIZATION, "Bearer " + token);
        var request = new RequestEntity<>(headers, HttpMethod.GET, URI.create(url));

        var response = restTemplate.exchange(request, KaKaoMemberInfoResponse.class);
        KaKaoMemberInfoResponse KaKaoMemberInfoResponse = Optional.ofNullable(response.getBody()).orElseThrow(() -> new ResponseBodyNullException(RESPONSE_BODY_NULL));

        Long id = KaKaoMemberInfoResponse.id();
        return id +"@kakao.com";
    }

    public boolean isKakaoTokenValid(String token){
        String url = "https://kapi.kakao.com/v1/user/access_token_info";

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.AUTHORIZATION, "Bearer " + token);

        var request = new RequestEntity<>(headers, HttpMethod.GET, URI.create(url));

        try {
            restTemplate.exchange(request, String.class);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public void sendKakaoMessage(String accessToken, String message) {
        var url = "https://kapi.kakao.com/v2/api/talk/memo/default/send";

        var headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_FORM_URLENCODED_VALUE);
        headers.add(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken);

        var body = new LinkedMultiValueMap<String, String>();
        StringBuilder templateObjectBuilder = new StringBuilder();
        templateObjectBuilder.append("{\"object_type\":\"text\",\"text\":\"")
                .append(message)
                .append("\",\"link\":{\"web_url\":\"\"}}");
        body.add("template_object", templateObjectBuilder.toString());

        var request = new RequestEntity<>(body, headers, HttpMethod.POST, URI.create(url));
        restTemplate.exchange(request,String.class);
    }
}