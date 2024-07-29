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

    public void sendMessageToMe(String token, String text)
        throws JsonProcessingException {
        var uri = kakaoApiSecurityProps.getMemoSend();
        var body = getSelfMessageRequestBody(text);
        client.post().uri(uri).contentType(MediaType.APPLICATION_FORM_URLENCODED)
            .body(body).contentType(MediaType.APPLICATION_JSON)
            .headers(httpHeaders -> httpHeaders.setBearerAuth(token)).retrieve().toBodilessEntity();
    }

    public MultiValueMap<String, String> getSelfMessageRequestBody(String text)
        throws JsonProcessingException {
        Map<String, String> templateObject = new HashMap<>();
        templateObject.put("object_type", "text");
        templateObject.put("text", text);
        templateObject.put("link", null);
        templateObject.put("button_title", "버튼");
        var jsonTemplateObject = objectMapper.writeValueAsString(templateObject);

        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("template_object", jsonTemplateObject);
        return body;
    }


}
