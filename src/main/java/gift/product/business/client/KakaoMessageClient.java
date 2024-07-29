package gift.product.business.client;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import gift.global.util.StringUtils;
import gift.oauth.business.client.KakaoApiClient;
import gift.product.business.dto.KakaoOrderMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClient;

@Component
public class KakaoMessageClient {

    private final RestClient restClient;
    private final Logger log = LoggerFactory.getLogger(KakaoApiClient.class);

    public KakaoMessageClient(ClientHttpRequestFactory clientHttpRequestFactory) {
        this.restClient = RestClient.builder()
            .requestFactory(clientHttpRequestFactory)
            .build();
    }

    @Async
    public void sendOrderMessage(String accessToken, KakaoOrderMessage.TemplateObject message) {
        var url = "https://kapi.kakao.com/v2/api/talk/memo/default/send";

        restClient.post()
            .uri(url)
            .header("Authorization", "Bearer " + accessToken)
            .contentType(MediaType.APPLICATION_FORM_URLENCODED)
            .body(getOrderMessageRequestBody(message))
            .retrieve()
            .onStatus(
                HttpStatusCode::isError,
                (request, response) -> {
                    log.error("Error Response Body: {}", StringUtils.convert(response.getBody()));
                    throw new RuntimeException("Failed to send order message to Kakao API.");
                }
            )
            .body(String.class);
    }

    private MultiValueMap<String, String> getOrderMessageRequestBody(
        KakaoOrderMessage.TemplateObject message) {
        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();

        var objectMapper = new ObjectMapper();
        var templateObjectJson = "";

        try {
            templateObjectJson = objectMapper.writeValueAsString(message);
        } catch (JsonProcessingException e) {
            log.error("Failed to convert KakaoOrderMessage to JSON: {}", e.getMessage());
            throw new RuntimeException("Failed to convert KakaoOrderMessage to JSON.");
        }

        body.add("template_object", templateObjectJson);

        return body;
    }

}
