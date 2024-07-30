package gift.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.HashMap;
import java.util.Map;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;


@Service
public class KakaoMessageService {

    @Value("${kakao.client-id}")
    private String clientId;

    @Value("${kakao.redirect-uri}")
    private String redirectUri;

    private final RestTemplate restTemplate;

    public KakaoMessageService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public void sendOrderMessage(String accessToken, String message) throws Exception {
        String url = "https://kapi.kakao.com/v2/api/talk/memo/default/send";

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(accessToken);
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        Map<String, Object> templateMap = new HashMap<>();
        templateMap.put("object_type", "text");
        templateMap.put("text", message);
        Map<String, String> linkMap = new HashMap<>();
        linkMap.put("web_url", "http://localhost:8080/");
        linkMap.put("mobile_web_url", "http://localhost:8080/");
        templateMap.put("link", linkMap);
        templateMap.put("button_title", "바로 확인");

        ObjectMapper objectMapper = new ObjectMapper();
        String templateObject = objectMapper.writeValueAsString(templateMap);

        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("template_object", templateObject);

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(body, headers);
        ResponseEntity<String> response = restTemplate.postForEntity(url, request, String.class);

        if (response.getStatusCode() != HttpStatus.OK) {
            throw new RuntimeException("카카오 메세지 전송 실패: " + response.getBody());
        }
    }

}
