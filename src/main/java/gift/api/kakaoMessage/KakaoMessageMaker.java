package gift.api.kakaoMessage;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import gift.order.Order;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;

@Component
public class KakaoMessageMaker {

    public Map<String, List<String>> createOrderMessage(Order order) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            LinkedMultiValueMap<String, String> body = new LinkedMultiValueMap<>();
            TextTemplate textTemplate = new TextTemplate("text", order.createOrderText(),
                new Link("http://15.165.164.139:8080", "http://15.165.164.139:8080"));
            body.add("template_object", objectMapper.writeValueAsString(textTemplate));
            return body;
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
