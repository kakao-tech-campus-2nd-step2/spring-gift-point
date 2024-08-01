package gift.domain;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import gift.dto.KakaoCommerceMessage;
import gift.exceptionHandler.RestClientErrorHandler;
import gift.properties.KakaoProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.client.RestClient;

@Component
public class KakaoMessageSender {

    private final KakaoProperties kakaoProperties;
    private final RestClient restClient = RestClient.create();
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final Logger logger = LoggerFactory.getLogger(KakaoMessageSender.class);

    public KakaoMessageSender(KakaoProperties kakaoProperties) {
        this.kakaoProperties = kakaoProperties;
    }

    public void sendForMe(String token, KakaoCommerceMessage message)
        throws JsonProcessingException {

        ResponseEntity<String> response = restClient.post()
            .uri(kakaoProperties.getSendMessageForMe())
            .contentType(MediaType.APPLICATION_FORM_URLENCODED)
            .header("Authorization", "Bearer " + token)
            .body(generateRequestBody(message))
            .retrieve()
            .onStatus(HttpStatusCode::is4xxClientError, RestClientErrorHandler.http4xxErrorHandler)
            .onStatus(HttpStatusCode::is5xxServerError, RestClientErrorHandler.http5xxErrorHandler)
            .toEntity(String.class);

        logger.info(response.getStatusCode().toString());
        logger.info(response.getBody());
    }

    private LinkedMultiValueMap<String, String> generateRequestBody(KakaoCommerceMessage message)
        throws JsonProcessingException {

        String json = objectMapper.writeValueAsString(message);
        LinkedMultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("template_object", json);

        return body;
    }
}