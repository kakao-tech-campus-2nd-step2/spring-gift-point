package gift.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import gift.dto.KakaoProperties;
import gift.dto.OrderDTO;
import gift.exception.KakaoOAuthException;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Service
public class KakaoUserService {
    private final KakaoProperties properties;
    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    public KakaoUserService(KakaoProperties properties, ObjectMapper objectMapper) {
        this.properties = properties;
        this.restTemplate = new RestTemplate();
        this.objectMapper = objectMapper;
    }

    public String getAuthorizeUrl() {
        return "https://kauth.kakao.com/oauth/authorize?client_id=" + properties.clientId() +
            "&redirect_uri=" + properties.redirectUrl() + "&response_type=code&scope=account_email";
    }

    public String getAccessToken(String code) {
        var url = "https://kauth.kakao.com/oauth/token";
        var body = new LinkedMultiValueMap<String, String>();
        body.add("grant_type", "authorization_code");
        body.add("client_id", properties.clientId());
        body.add("redirect_uri", properties.redirectUrl());
        body.add("code", code);

        try {
            return restTemplate.postForObject(url, body, String.class);
        } catch (HttpClientErrorException e) {
            throw new KakaoOAuthException(e.getStatusCode().toString(), e.getStatusCode());
        }
    }

    public Map<String, Object> getKakaoUserInfo(String accessToken) {
        var url = "https://kapi.kakao.com/v2/user/me";
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", "Bearer " + accessToken);
            HttpEntity<Void> requestEntity = new HttpEntity<>(headers);

            ResponseEntity<Map> response = restTemplate.exchange(url, HttpMethod.GET, requestEntity, Map.class);
            return response.getBody();
        } catch (HttpStatusCodeException e) {
            if (e.getStatusCode() == HttpStatus.UNAUTHORIZED) {
                throw new KakaoOAuthException("토큰이 유효하지 않습니다.", e.getStatusCode());
            }
            throw new KakaoOAuthException(e.getStatusCode().toString(), e.getStatusCode());
        }
    }

    public void sendOrderMessage(String accessToken, OrderDTO order) throws JsonProcessingException {
        HttpEntity<MultiValueMap<String, String>> requestEntity = createRequestEntity(accessToken, order);
        sendRequest(requestEntity);
    }

    private HttpEntity<MultiValueMap<String, String>> createRequestEntity(String accessToken, OrderDTO order) throws JsonProcessingException {
        String templateObjectJson = createTemplate(order);

        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("template_object", templateObjectJson);

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + accessToken);
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        return new HttpEntity<>(body, headers);
    }

    private void sendRequest(HttpEntity<MultiValueMap<String, String>> requestEntity) {
        String url = "https://kapi.kakao.com/v2/api/talk/memo/default/send";
        try {
            restTemplate.postForObject(url, requestEntity, String.class);
        } catch (HttpStatusCodeException e) {
            throw new KakaoOAuthException(e.getStatusCode().toString(), e.getStatusCode());
        }
    }


    private String createTemplate(OrderDTO order) throws JsonProcessingException {
        String orderDetails = String.format("옵션 id: %s, 수량: %d, 메시지: %s",
            order.getOptionId(), order.getQuantity(), order.getMessage());

        Map<String, Object> message = new HashMap<>();
        message.put("object_type", "text");
        message.put("text", "주문이 완료되었습니다.\n주문 내용: " + orderDetails);
        message.put("link", Map.of(
            "web_url", "www.naver.com",
            "mobile_web_url", "www.google.com"
        ));

        return objectMapper.writeValueAsString(message);
    }
}