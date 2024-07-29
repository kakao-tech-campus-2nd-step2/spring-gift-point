package gift.application.product.service.apiCaller;

import static io.jsonwebtoken.Header.CONTENT_TYPE;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import gift.application.product.service.apiCaller.dto.ProductKakaoModel.Link;
import gift.application.product.service.apiCaller.dto.ProductKakaoModel.TemplateObject;
import gift.global.config.KakaoProperties;
import java.net.URI;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.client.RestTemplate;

@Component
public class ProductKakaoApiCaller {

    private final ObjectMapper objectMapper;
    private final KakaoProperties kakaoProperties;
    private final RestTemplate restTemplate;

    public ProductKakaoApiCaller(RestTemplate restTemplate,
        KakaoProperties kakaoProperties, ObjectMapper objectMapper) {
        this.restTemplate = restTemplate;
        this.kakaoProperties = kakaoProperties;
        this.objectMapper = objectMapper;
    }

    public void sendMessage(String accessToken, String optionName) {
        var headers = createFormUrlencodedHttpHeaders(accessToken);
        var body = createSendMessageBody(optionName);
        var request = new RequestEntity<>(body, headers, HttpMethod.POST,
            URI.create(kakaoProperties.messageRequestUri()));

        try {
            var statusCode = restTemplate.exchange(request, JsonNode.class).getStatusCode();
            if (statusCode != HttpStatus.OK) {
                throw new RuntimeException("메시지 전송에 실패했습니다.");
            }
        } catch (Exception e) {
            throw new RuntimeException("메시지 전송에 실패했습니다.", e);
        }

    }

    private LinkedMultiValueMap<Object, Object> createSendMessageBody(
        String optionName) {
        var body = new LinkedMultiValueMap<>();

        TemplateObject templateObject = new TemplateObject("text", makePurchaseMessage(optionName),
            new Link(null, null));

        String templateObjectJson;
        try {
            templateObjectJson = objectMapper.writeValueAsString(templateObject);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("메시지 전송에 실패했습니다.", e);
        }
        body.set("template_object", templateObjectJson);
        return body;
    }

    private static HttpHeaders createFormUrlencodedHttpHeaders(String accessToken) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + accessToken);
        headers.add(CONTENT_TYPE, MediaType.APPLICATION_FORM_URLENCODED_VALUE);
        return headers;
    }

    private String makePurchaseMessage(String optionName) {
        return optionName + " 상품이 구매되었습니다.";
    }
}


