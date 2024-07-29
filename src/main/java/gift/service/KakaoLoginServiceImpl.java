package gift.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import gift.dto.KakaoUserDTO;
import gift.dto.Response.AccessTokenResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import java.nio.charset.StandardCharsets;
import java.util.List;

@Service
public class KakaoLoginServiceImpl implements KakaoLoginService {

    @Value("${kakao.client-id}")
    private String clientId;

    @Value("${kakao.redirect-uri}")
    private String redirectUri;

    private static final String KAKAO_TOKEN_URL = "https://kauth.kakao.com/oauth/token";
    private static final String KAKAO_USER_URL = "https://kapi.kakao.com/v2/user/me";
    private static final String KAKAO_MESSAGE_URL = "https://kapi.kakao.com/v2/api/talk/memo/default/send";

    @Bean
    public RestTemplate restTemplate() {
        SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
        factory.setConnectTimeout(5000);
        factory.setReadTimeout(5000);
        return new RestTemplate(factory);
    }

    @Override
    public AccessTokenResponse getAccessToken(String code) {
        RestTemplate restTemplate = restTemplate();
        HttpHeaders headers = createHeaders(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> params = createTokenRequestParams(code);

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(params, headers);
        ResponseEntity<AccessTokenResponse> response = restTemplate.postForEntity(KAKAO_TOKEN_URL,
            request, AccessTokenResponse.class);

        if (!response.getStatusCode().is2xxSuccessful()) {
            throw new RuntimeException("Failed to get access token: " + response.getStatusCode());
        }

        AccessTokenResponse accessTokenResponse = response.getBody();
        return accessTokenResponse;
    }

    @Override
    public KakaoUserDTO getUserInfo(String accessToken) {
        RestTemplate restTemplate = restTemplate();
        HttpHeaders headers = createHeadersWithBearerAuth(accessToken);

        HttpEntity<String> userInfoRequest = new HttpEntity<>(headers);
        ResponseEntity<KakaoUserDTO> userInfoResponse = restTemplate.exchange(KAKAO_USER_URL,
            HttpMethod.GET, userInfoRequest, KakaoUserDTO.class);

        if (!userInfoResponse.getStatusCode().is2xxSuccessful()) {
            throw new RuntimeException(
                "Failed to retrieve user information: " + userInfoResponse.getStatusCode());
        }

        return userInfoResponse.getBody();
    }

    @Override
    public String extractNickname(KakaoUserDTO userInfo) {
        if (userInfo == null || userInfo.getProperties() == null) {
            return null;
        }
        return userInfo.getProperties().getNickname();
    }

    @Override
    public void sendMessage(String accessToken, String message) {
        RestTemplate restTemplate = restTemplate();
        HttpHeaders headers = createHeadersWithBearerAuth(accessToken);

        // Jackson 라이브러리로 직렬화
        KakaoMessageTemplate template = new KakaoMessageTemplate("text", message,
            new KakaoMessageLink("http://localhost:8080", "http://localhost:8080"));
        String templateObject;
        try {
            templateObject = new ObjectMapper().writeValueAsString(template);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Failed to serialize message template", e);
        }

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("template_object", templateObject);

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(params, headers);
        restTemplate.postForEntity(KAKAO_MESSAGE_URL, request, String.class);
    }

    // 로그인 시 메시지를 보내는 메서드 추가
    public void sendLoginMessage(String accessToken, String nickname) {
        String message = String.format("%s님이 Spring-gift-order에 로그인했습니다.", nickname);
        sendMessage(accessToken, message);
    }

    private HttpHeaders createHeaders(MediaType contentType) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(contentType);
        headers.setAcceptCharset(List.of(StandardCharsets.UTF_8));
        return headers;
    }

    private HttpHeaders createHeadersWithBearerAuth(String accessToken) {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(accessToken);
        headers.setAcceptCharset(List.of(StandardCharsets.UTF_8));
        return headers;
    }

    private MultiValueMap<String, String> createTokenRequestParams(String code) {
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("grant_type", "authorization_code");
        params.add("client_id", clientId);
        params.add("redirect_uri", redirectUri);
        params.add("code", code);
        return params;
    }

    // 메시지 템플릿 추가
    static class KakaoMessageTemplate {

        private String object_type;
        private String text;
        private KakaoMessageLink link;

        public KakaoMessageTemplate(String object_type, String text, KakaoMessageLink link) {
            this.object_type = object_type;
            this.text = text;
            this.link = link;
        }

        public String getObject_type() {
            return object_type;
        }

        public String getText() {
            return text;
        }

        public KakaoMessageLink getLink() {
            return link;
        }

        public void setObject_type(String object_type) {
            this.object_type = object_type;
        }

        public void setText(String text) {
            this.text = text;
        }

        public void setLink(KakaoMessageLink link) {
            this.link = link;
        }
    }

    // KakaoMessageLink 클래스 추가
    static class KakaoMessageLink {

        private String web_url;
        private String mobile_web_url;

        public KakaoMessageLink(String web_url, String mobile_web_url) {
            this.web_url = web_url;
            this.mobile_web_url = mobile_web_url;
        }

        public String getWeb_url() {
            return web_url;
        }

        public String getMobile_web_url() {
            return mobile_web_url;
        }

        public void setWeb_url(String web_url) {
            this.web_url = web_url;
        }

        public void setMobile_web_url(String mobile_web_url) {
            this.mobile_web_url = mobile_web_url;
        }
    }
}
