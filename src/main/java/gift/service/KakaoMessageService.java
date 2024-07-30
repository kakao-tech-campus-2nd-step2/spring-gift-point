package gift.service;

import gift.model.order.Order;
import gift.model.order.OrderRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.client.RestTemplate;

@Service
public class KakaoMessageService {

    private final RestTemplate restTemplate;

    public KakaoMessageService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public void sendMessageToMe(String accessToken, Order order) {
        var url = "https://kapi.kakao.com/v2/api/talk/memo/default/send";
        var header = new HttpHeaders();
        header.setBearerAuth(accessToken);
        header.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        var body = createMessageBody(order);
        var request = new HttpEntity<>(body, header);

        restTemplate.exchange(url, HttpMethod.POST, request, String.class);
    }

    private LinkedMultiValueMap<String, String> createMessageBody(Order order) {
        var body = new LinkedMultiValueMap<String, String>();
        var templateObject = String.format(
            "{\"object_type\": \"text\", \"text\": \"Order ID: %d\\nMessage: %s\", \"link\": {\"web_url\": \"\"}}",
            order.getId(), order.getMessage()
        );
        body.add("template_object", templateObject);
        return body;
    }

}
