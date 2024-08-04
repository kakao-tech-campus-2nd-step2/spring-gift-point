package gift.oauth;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.HashMap;
import java.util.Map;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClient;

@Service
public class KakaoApiService {

    private final KakaoApiSecurityProperties kakaoApiSecurityProps;
    private final RestClient client;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public KakaoApiService(KakaoApiSecurityProperties kakaoApiSecurityProps,
        RestClient restClient) {
        this.kakaoApiSecurityProps = kakaoApiSecurityProps;
        this.client = restClient;
    }

    public void sendMessageToMe(String token, String text) throws JsonProcessingException {
        var uri = kakaoApiSecurityProps.getMemoSend();
        String templateObject = getSelfMessageRequestBody(text);

        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("template_object", templateObject);

        client.post()
            .uri(uri)
            .contentType(MediaType.APPLICATION_FORM_URLENCODED)
            .body(body)
            .headers(httpHeaders -> {
                httpHeaders.setBearerAuth(token);
            })
            .retrieve()
            .toEntity(String.class);
    }

    public String getSelfMessageRequestBody(String text) throws JsonProcessingException {
        Map<String, Object> link = new HashMap<>();
        link.put("web_url", "");
        link.put("mobile_web_url", "");

        Map<String, Object> templateObject = new HashMap<>();
        templateObject.put("object_type", "text");
        templateObject.put("text", text);
        templateObject.put("link", link);
        templateObject.put("button_title", "바로 확인");

        return objectMapper.writeValueAsString(templateObject);
    }


}
