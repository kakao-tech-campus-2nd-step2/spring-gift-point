package gift.service;

import gift.config.KakaoProperties;
import java.net.URI;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class KakaoMessageService {

    private final KakaoProperties kakaoProperties;
    private final RestTemplate restTemplate;

    public KakaoMessageService(KakaoProperties kakaoProperties, RestTemplate restTemplate) {
        this.kakaoProperties = kakaoProperties;
        this.restTemplate = restTemplate;
    }

    public void sendOrderMessage(String message, String productName, Integer quantity, Integer totalPrice) {
        String url = "https://kapi.kakao.com/v2/api/talk/memo/default/send";
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_FORM_URLENCODED_VALUE);
        headers.setBearerAuth(kakaoProperties.getAccessToken());

        String templateObject = createTemplateObject(message, productName, quantity, totalPrice);
        String encodedTemplateObject = URLEncoder.encode(templateObject, StandardCharsets.UTF_8);

        RequestEntity<String> request = new RequestEntity<>(
            "template_object=" + encodedTemplateObject,
            headers, HttpMethod.POST, URI.create(url));

        restTemplate.exchange(request, String.class);
    }

    private String createTemplateObject(String message, String productName, Integer quantity, Integer totalPrice) {
        return """
                {
                    "object_type": "feed",
                    "content": {
                        "title": "주문해 주셔서 감사합니다.",
                        "description": "%s",
                        "image_url": "https://avatars.githubusercontent.com/u/161289673?v=4",
                        "image_width": 640,
                        "image_height": 640,
                        "link": {}
                    },
                    "item_content": {
                        "items": [
                            {"item": "상품명", "item_op": "%s"},
                            {"item": "수량", "item_op": "%d개"},
                            {"item": "가격", "item_op": "%d원"}
                        ]
                    }
                }
            """.formatted(message, productName, quantity, totalPrice);
    }
}
