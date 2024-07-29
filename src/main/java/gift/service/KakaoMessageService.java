package gift.service;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClient;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Map;
import java.util.HashMap;

@Service
public class KakaoMessageService {

    private final String KAKAO_API_URL = "https://kapi.kakao.com/v2/api/talk/memo/default/send";
    private final RestClient restClient;
    private final ObjectMapper objectMapper;

    public KakaoMessageService(ObjectMapper objectMapper) {
        this.restClient = RestClient.create();
        this.objectMapper = objectMapper;
    }

    public void sendOrderMessage(String accessToken, String productName, String optionName, int quantity, String message, String imageUrl) {
        try {
            String templateObject = createTemplateObject(productName, optionName, quantity, message, imageUrl);

            MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
            body.add("template_object", templateObject);

            restClient.post()
                .uri(KAKAO_API_URL)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(body)
                .retrieve()
                .toBodilessEntity();
        } catch (Exception e) {
            throw new RuntimeException("카카오톡 메시지 전송에 실패했습니다.", e);
        }
    }

    private String createTemplateObject(String productName, String optionName, int quantity, String message, String imageUrl) throws Exception {
        Map<String, Object> templateObject = new HashMap<>();
        templateObject.put("object_type", "feed");

        Map<String, Object> content = new HashMap<>();
        content.put("title", "주문이 완료되었습니다");
        content.put("description", String.format("상품명: %s\n옵션: %s\n수량: %d\n메시지: %s", productName, optionName, quantity, message));
        content.put("image_url", imageUrl);
        content.put("image_width", 640);
        content.put("image_height", 640);

        Map<String, Object> link = new HashMap<>();
        link.put("web_url", "http://localhost:8080/admin");
        link.put("mobile_web_url", "http://localhost:8080/admin");
        content.put("link", link);

        templateObject.put("content", content);

        Map<String, Object> button = new HashMap<>();
        button.put("title", "주문 상세보기");
        button.put("link", link);

        templateObject.put("buttons", new Object[]{button});

        return objectMapper.writeValueAsString(templateObject);
    }
}