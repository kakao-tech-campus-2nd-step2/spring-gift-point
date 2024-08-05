package gift.doamin.order.util;

import static org.springframework.http.MediaType.APPLICATION_FORM_URLENCODED;

import com.fasterxml.jackson.databind.ObjectMapper;
import gift.doamin.order.dto.Link;
import gift.doamin.order.dto.MessageTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClient;

@Component
public class KakaoMessageRestClient {

    private static final String URL = "https://kapi.kakao.com/v2/api/talk/memo/default/send";

    private final RestClient restClient;

    public KakaoMessageRestClient() {
        this.restClient = RestClient.create();
    }

    public void sendMessage(String kakaoAccessToken, String message) {

        MultiValueMap<String, Object> body = getMessageRequestBody(message);

        restClient.post()
            .uri(URL)
            .contentType(APPLICATION_FORM_URLENCODED)
            .header("Authorization", "Bearer " + kakaoAccessToken)
            .body(body)
            .retrieve()
            .toEntity(String.class);
    }

    private static MultiValueMap<String, Object> getMessageRequestBody(String message) {
        ObjectMapper objectMapper = new ObjectMapper();
        MessageTemplate messageTemplate = new MessageTemplate("text", message,
            new Link(""));
        String messageJson;
        try {
            messageJson = objectMapper.writeValueAsString(messageTemplate);
        } catch (Exception e) {
            throw new IllegalArgumentException();
        }

        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        body.add("template_object", messageJson);

        return body;
    }

}
