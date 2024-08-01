package gift.Service;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Service
public class KakaoMessageService {

    private final String KAKAO_API_URL = "https://kapi.kakao.com/v2/api/talk/memo/default/send";

    //카카오톡 메시지 보내기 API를 이용하여, 나에게 메시지 보내기 기능을 사용하는 메서드
    public void sendMessage(String accessToken, String message) {
        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + accessToken);
        headers.set("Content-Type", "application/x-www-form-urlencoded");

        Map<String, String> templateObject = new HashMap<>();
        templateObject.put("object_type", "text");
        templateObject.put("text", message);
        templateObject.put("link", "{\"web_url\":\"http://localhost:8080\",\"mobile_web_url\":\"http://localhost:8080\"}");

        Map<String, Object> params = new HashMap<>();
        params.put("template_object", templateObject);

        HttpEntity<Map<String, Object>> request = new HttpEntity<>(params, headers);

        ResponseEntity<String> response = restTemplate.exchange(KAKAO_API_URL, HttpMethod.POST, request, String.class);

        if (!response.getStatusCode().is2xxSuccessful()) {
            throw new RuntimeException("카카오에게 메시지 API요청 실패함: " + response.getBody());
        }
    }
}
