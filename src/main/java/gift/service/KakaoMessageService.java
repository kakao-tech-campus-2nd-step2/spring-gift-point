package gift.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import gift.dto.OrderResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.client.ResponseErrorHandler;

import java.io.IOException;
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
        this.kakaoRestTemplate.setErrorHandler(new CustomResponseErrorHandler());
    }

    public void sendMessage(OrderResponse order, String accessToken) {
        try {
            String message = createMessage(order);  // 주문 정보로부터 메시지 생성
            String templateObjectJson = createTemplateObjectJson(message);  // 메시지 JSON 생성

            HttpEntity<MultiValueMap<String, String>> request = createRequestEntity(templateObjectJson, accessToken);  // 요청 엔티티 생성
            ResponseEntity<String> response = sendKakaoRequest(request);  // 카카오 요청 전송

            handleResponse(response);  // 응답 처리
        } catch (JsonProcessingException e) {
            logger.error("JSON 처리 중 오류 발생: {}", e.getMessage());
            throw new RuntimeException("template_object를 JSON으로 변환하는 중 오류가 발생했습니다.", e);
        }
    }

    // 주문 정보로부터 메시지 생성
    private String createMessage(OrderResponse order) {
        return String.format("OptionId: %d\nQuantity: %d\nOrder Date: %s\nMessage: %s",
                order.getOptionId(), order.getQuantity(), order.getOrderDateTime().toString(), order.getMessage());
    }

    // 메시지 JSON 생성
    private String createTemplateObjectJson(String message) throws JsonProcessingException {
        Map<String, String> link = new HashMap<>();
        link.put("web_url", "http://www.example.com");
        link.put("mobile_web_url", "http://www.example.com");

        KakaoMessageTemplate templateObject = new KakaoMessageTemplate("text", message, link);
        String templateObjectJson = objectMapper.writeValueAsString(templateObject);
        logger.info("Template Object JSON: {}", templateObjectJson);

        return templateObjectJson;
    }

    // 요청 엔티티 생성
    private HttpEntity<MultiValueMap<String, String>> createRequestEntity(String templateObjectJson, String accessToken) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + accessToken);
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("template_object", templateObjectJson);

        return new HttpEntity<>(body, headers);
    }

    // 카카오 요청 전송
    private ResponseEntity<String> sendKakaoRequest(HttpEntity<MultiValueMap<String, String>> request) {
        logger.info("Sending Kakao message with access token");
        return kakaoRestTemplate.postForEntity(messageUrl, request, String.class);
    }

    // 응답 처리
    private void handleResponse(ResponseEntity<String> response) {
        if (response.getStatusCode() != HttpStatus.OK) {
            logger.error("Kakao message response body: {}", response.getBody());
            throw new RuntimeException("카카오 메시지 전송에 실패했습니다.: " + response.getBody());
        }
        logger.info("Kakao message response: {}", response);
    }

    private static class CustomResponseErrorHandler implements ResponseErrorHandler {
        private final Logger logger = LoggerFactory.getLogger(CustomResponseErrorHandler.class);

        @Override
        public boolean hasError(ClientHttpResponse response) throws IOException {
            return response.getStatusCode().isError();
        }

        @Override
        public void handleError(ClientHttpResponse response) throws IOException {
            HttpStatus statusCode = HttpStatus.resolve(response.getStatusCode().value());

            Map<HttpStatus, String> errorMessages = Map.of(
                    HttpStatus.BAD_REQUEST, "Bad Request",
                    HttpStatus.NOT_FOUND, "Not Found",
                    HttpStatus.INTERNAL_SERVER_ERROR, "Internal Server Error"
            );

            String errorMessage = errorMessages.getOrDefault(statusCode, "Unexpected error occurred");
            String responseBody = new String(response.getBody().readAllBytes());
            logger.error("Failed to send Kakao message, HTTP Status: {}, Response Body: {}", statusCode, responseBody);
            throw new RuntimeException("카카오 메시지 전송 중 예외가 발생했습니다.: " + errorMessage + ": " + responseBody);
        }
    }
}