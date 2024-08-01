package gift.service;

import gift.config.KakaoProperties;
import gift.model.Order;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

@Service
public class KakaoMessageService {

    private final RestTemplate restTemplate;
    private final KakaoProperties kakaoProperties;

    public KakaoMessageService(RestTemplate restTemplate, KakaoProperties kakaoProperties) {
        this.restTemplate = restTemplate;
        this.kakaoProperties = kakaoProperties;
    }

    public void sendOrderMessage(Order order, String accessToken) {
        String url = "https://kapi.kakao.com/v2/api/talk/memo/default/send";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        headers.set("Authorization", "Bearer " + accessToken);

        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("template_object", createTemplateJson(order));

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(body, headers);

        restTemplate.exchange(url, HttpMethod.POST, request, String.class);
    }

    private String createTemplateJson(Order order) {
        return "{"
                + "\"object_type\":\"feed\","
                + "\"content\":{"
                + "\"title\":\"주문이 완료되었습니다.\","
                + "\"description\":\"옵션: " + order.getOption().getName() + "\\n수량: " + order.getQuantity() + "\\n메시지: " + order.getMessage() + "\","
                + "\"link\":{"
                + "\"web_url\":\"http://developer.kakao.com\","
                + "\"mobile_web_url\":\"http://developer.kakao.com\""
                + "},"
                + "\"button_title\":\"주문 확인\""
                + "}"
                + "}";
    }
}
