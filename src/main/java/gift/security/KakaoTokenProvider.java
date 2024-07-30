package gift.security;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Component
public class KakaoTokenProvider {

    @Value("${kakao.client-id}")
    private String clientId;

    @Value("${kakao.redirect-uri}")
    private String redirectUri;

    private final RestTemplate restTemplate = new RestTemplate();

    // 새로운 액세스 토큰을 발급받는 메소드
    public String getToken(String code) throws Exception {
        var url = "https://kauth.kakao.com/oauth/token";
        var body = new LinkedMultiValueMap<String, String>();
        body.add("grant_type", "authorization_code");
        body.add("client_id", clientId);
        body.add("redirect_uri", redirectUri);
        body.add("code", code);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        ResponseEntity<String> entity = restTemplate.postForEntity(url, new HttpEntity<>(body, headers), String.class);

        String resBody = entity.getBody();
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(resBody);

        if (jsonNode.has("error")) {
            throw new RuntimeException("액세스 토큰 발급 실패: " + jsonNode.get("error_description").asText());
        }

        return jsonNode.get("access_token").asText();
    }

    // 카카오 메시지 전송
    public void sendMessage(String accessToken, String message) throws Exception {
        var url = "https://kapi.kakao.com/v2/api/talk/memo/default/send";

        Map<String, Object> templateObject = new HashMap<>();
        templateObject.put("object_type", "text");
        templateObject.put("text", message);
        templateObject.put("link", Map.of("web_url", "http://localhost:8080", "mobile_web_url", "http://localhost:8080"));

        ObjectMapper objectMapper = new ObjectMapper();
        String templateObjectJson = objectMapper.writeValueAsString(templateObject);

        var body = new LinkedMultiValueMap<String, String>();
        body.add("template_object", templateObjectJson);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        headers.set(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken);

        ResponseEntity<String> entity = restTemplate.postForEntity(url, new HttpEntity<>(body, headers), String.class);

        if (entity.getStatusCode().isError()) {
            throw new RuntimeException("카카오 메시지 전송 실패: " + entity.getBody());
        }
    }
}