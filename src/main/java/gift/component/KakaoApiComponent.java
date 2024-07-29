package gift.component;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import gift.dto.kakao.KakaoAccessTokenResponse;
import gift.dto.kakao.KakaoMessageRequest;
import gift.dto.kakao.KakaoProperties;
import gift.dto.kakao.KakaoUserInfoResponse;
import gift.exception.auth.UnauthorizedException;
import org.apache.logging.log4j.util.InternalException;
import org.json.JSONObject;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.net.URI;

@Component
public class KakaoApiComponent {
    private final KakaoProperties kakaoProperties;
    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    public KakaoApiComponent(KakaoProperties kakaoProperties, RestTemplate restTemplate, ObjectMapper objectMapper) {
        this.kakaoProperties = kakaoProperties;
        this.restTemplate = restTemplate;
        this.objectMapper = objectMapper;
    }

    public String getAccessToken(String code) {
        String url = "https://kauth.kakao.com/oauth/token";
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_FORM_URLENCODED_VALUE);

        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("grant_type", "authorization_code");
        body.add("client_id", kakaoProperties.clientId());
        body.add("redirect_uri", kakaoProperties.redirectUrl());
        body.add("code", code);

        RequestEntity<MultiValueMap<String, String>> request = new RequestEntity<>(body, headers, HttpMethod.POST, URI.create(url));
        ResponseEntity<KakaoAccessTokenResponse> response = restTemplate.exchange(request, KakaoAccessTokenResponse.class);

        if (response.getBody().scope() == null || !response.getBody().scope().equals("talk_message")) {
            throw new UnauthorizedException("[spring-gift] App disabled [talk_message] scopes for [TALK_MEMO_DEFAULT_SEND] API on developers.kakao.com. Enable it first.");
        }
        return response.getBody().accessToken();
    }

    public String getMemberProfileId(String accessToken) {
        String url = "https://kapi.kakao.com/v2/user/me";
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken);
        headers.add(HttpHeaders.CONTENT_TYPE, "application/x-www-form-urlencoded;charset=utf-8");

        HttpEntity<Object> kakaoProfileRequest = new HttpEntity<>(headers);
        ResponseEntity<KakaoUserInfoResponse> response = restTemplate.exchange(url, HttpMethod.POST, kakaoProfileRequest, KakaoUserInfoResponse.class);

        return response.getBody().id().toString();
    }

    public void sendMessage(String accessToken, KakaoMessageRequest kakaoMessageRequest) {
        String url = "https://kapi.kakao.com/v2/api/talk/memo/default/send";
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken);
        headers.add(HttpHeaders.CONTENT_TYPE, "application/x-www-form-urlencoded;charset=utf-8");


        JSONObject linkObj = new JSONObject();
        linkObj.put("web_url", kakaoMessageRequest.webUrl());
        linkObj.put("mobile_web_url", kakaoMessageRequest.mobileWebUrl());

        JSONObject templateObj = new JSONObject();
        templateObj.put("object_type", kakaoMessageRequest.objectType());
        templateObj.put("text", kakaoMessageRequest.text());
        templateObj.put("link", linkObj);

        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("template_object", templateObj.toString());

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(body, headers);
        restTemplate.exchange(url, HttpMethod.POST, request, String.class);
    }
}
