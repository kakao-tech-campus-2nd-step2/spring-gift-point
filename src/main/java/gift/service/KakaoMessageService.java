package gift.service;

import gift.domain.Order;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.*;

import java.util.HashMap;
import java.util.Map;

@Service
public class KakaoMessageService {

    private static final String SEND_MESSAGE_SUFFIX = "/v2/api/talk/memo/default/send";
    private static final String WEB_URL = "http://localhost:8080";

    @Value("${kakao.api.url}")
    private String kakaoApiUrl;

    public void sendOrderMessage(String token, Order order) {
        String url = kakaoApiUrl + SEND_MESSAGE_SUFFIX;

        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(token);

        Map<String, Object> templateObject = new HashMap<>();
        templateObject.put("object_type", "text");
        templateObject.put("text", "주문이 완료되었습니다!\n" +
                "\n상품 옵션 ID: " + order.getOptionId() +
                "\n수량: " + order.getQuantity() +
                "\n메시지: " + order.getMessage());
        templateObject.put("link", new HashMap<>() {{
            put("web_url", WEB_URL);
            put("mobile_web_url", WEB_URL);
        }});
        templateObject.put("button_title", "주문 확인");

        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("template_object", templateObject);

        HttpEntity<Map<String, Object>> request = new HttpEntity<>(requestBody, headers);

        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, request, String.class);

        if (response.getStatusCode() != HttpStatus.OK) {
            throw new RuntimeException("카카오톡 보내기 실패: " + response.getBody());
        }
    }
}
