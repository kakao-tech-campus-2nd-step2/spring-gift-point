package gift.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import gift.dto.OrderResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Service
public class KakaoMessageService {

    private static final Logger logger = LoggerFactory.getLogger(KakaoMessageService.class);

    @Value("${kakao.message-url}")
    private String messageUrl;

    private final RestTemplate kakaoRestTemplate;
    private final ObjectMapper objectMapper;

    public KakaoMessageService(RestTemplate kakaoRestTemplate) {
        this.kakaoRestTemplate = kakaoRestTemplate;
        this.objectMapper = new ObjectMapper();
    }

    public void sendMessage(OrderResponse order, String accessToken) {
        String message = String.format("OptionId: %d\nQuantity: %d\nOrder Date: %s\nMessage: %s",
                order.getOptionId(), order.getQuantity(), order.getOrderDateTime().toString(), order.getMessage());

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + accessToken);
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        Map<String, String> link = new HashMap<>();
        link.put("web_url", "http://www.example.com");
        link.put("mobile_web_url", "http://www.example.com");

        KakaoMessageTemplate templateObject = new KakaoMessageTemplate("text", message, link);

        try {
            String templateObjectJson = objectMapper.writeValueAsString(templateObject);
            logger.info("Template Object JSON: {}", templateObjectJson);

            MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
            body.add("template_object", templateObjectJson);

            HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(body, headers);

            ResponseEntity<String> response;
            try {
                logger.info("Sending Kakao message with access token: {}", accessToken);
                response = kakaoRestTemplate.postForEntity(messageUrl, request, String.class);
                logger.info("Kakao message response: {}", response);
            } catch (HttpClientErrorException e) {
                logger.error("Failed to send Kakao message", e);
                logger.error("HTTP Status: {}", e.getStatusCode());
                logger.error("Response Body: {}", e.getResponseBodyAsString());
                throw new RuntimeException("카카오 메시지 전송 중 예외가 발생했습니다.: " + e.getMessage(), e);
            }

            if (response.getStatusCode() != HttpStatus.OK) {
                logger.error("Kakao message response body: {}", response.getBody());
                throw new RuntimeException("카카오 메시지 전송에 실패했습니다.: " + response.getBody());
            }
        } catch (JsonProcessingException e) {
            logger.error("JSON 처리 중 오류 발생: {}", e.getMessage());
            throw new RuntimeException("template_object를 JSON으로 변환하는 중 오류가 발생했습니다.", e);
        } catch (Exception e) {
            logger.error("예상치 못한 오류 발생: {}", e.getMessage());
            throw new RuntimeException("예상치 못한 오류가 발생했습니다.", e);
        }
    }
}