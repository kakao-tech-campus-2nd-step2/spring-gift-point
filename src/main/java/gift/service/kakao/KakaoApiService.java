package gift.service.kakao;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import gift.exception.ErrorCode;
import gift.exception.GiftException;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.Optional;

import static gift.exception.ErrorCode.*;
import static org.springframework.http.HttpMethod.GET;
import static org.springframework.http.HttpMethod.POST;
import static org.springframework.http.MediaType.APPLICATION_FORM_URLENCODED;

@Service
public class KakaoApiService {

    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;
    private final KakaoProperties kakaoProperties;

    public KakaoApiService(RestTemplate restTemplate, ObjectMapper objectMapper, KakaoProperties kakaoProperties) {
        this.restTemplate = restTemplate;
        this.objectMapper = objectMapper;
        this.kakaoProperties = kakaoProperties;
    }

    public void sendKakaoMessage(String accessToken, String message) {
        String url = "https://kapi.kakao.com/v2/api/talk/memo/default/send";

        HttpHeaders headers = createHeaders(accessToken);
        LinkedMultiValueMap<String, String> body = createRequestBody(message);

        HttpEntity<LinkedMultiValueMap<String, String>> httpEntity = new HttpEntity<>(body, headers);
        ResponseEntity<String> response = restTemplate.exchange(url, POST, httpEntity, String.class);

        if (!response.getStatusCode().is2xxSuccessful()) {
            throw new GiftException(SEND_KAKAO_MESSAGE_FAILED);
        }
    }

    public KakaoTokenInfoResponse getTokenInfo(String code) {
        String url = "https://kauth.kakao.com/oauth/token";
        LinkedMultiValueMap<String, String> body = createFormBody(code);

        return Optional.ofNullable(restTemplate.postForObject(url, body, KakaoTokenInfoResponse.class))
                .orElseThrow(() -> new GiftException(ErrorCode.KAKAO_TOKEN_ISSUANCE_FAILED));
    }

    public ResponseEntity<String> getUserInfo(String accessToken) {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(accessToken);
        HttpEntity<Void> request = new HttpEntity<>(headers);

        return restTemplate.exchange("https://kapi.kakao.com/v2/user/me", GET, request, String.class);
    }

    private LinkedMultiValueMap<String, String> createFormBody(String code) {
        LinkedMultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("grant_type", "authorization_code");
        body.add("client_id", kakaoProperties.clientId());
        body.add("redirect_uri", kakaoProperties.redirectUrl());
        body.add("code", code);
        return body;
    }

    public String createLoginUrl() {
        return "https://kauth.kakao.com/oauth/authorize?response_type=code"
                + "&redirect_uri=" + kakaoProperties.redirectUrl()
                + "&client_id=" + kakaoProperties.clientId();
    }

    public JsonNode parseJson(String body) {
        try {
            return objectMapper.readTree(body);
        } catch (JsonProcessingException e) {
            throw new GiftException(JSON_PARSING_FAILED);
        }
    }

    private HttpHeaders createHeaders(String accessToken) {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(accessToken);
        headers.setContentType(APPLICATION_FORM_URLENCODED);
        return headers;
    }

    private LinkedMultiValueMap<String, String> createRequestBody(String message) {
        KakaoMessageRequest.Link link = new KakaoMessageRequest.Link("https://example.com");
        KakaoMessageRequest request = new KakaoMessageRequest("text", message, link);

        LinkedMultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        try {
            String templateObject = objectMapper.writeValueAsString(request);
            body.add("template_object", templateObject);
        } catch (JsonProcessingException e) {
            throw new GiftException(JSON_PROCESSING_FAILED);
        }
        return body;
    }

}
