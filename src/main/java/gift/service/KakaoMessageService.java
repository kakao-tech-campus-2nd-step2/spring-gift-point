package gift.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class KakaoMessageService {

    private static final Logger logger = LoggerFactory.getLogger(KakaoMessageService.class);
    private static final String HEADER_CONTENT_TYPE = "Content-Type";
    private static final String HEADER_AUTHORIZATION = "Authorization";
    private static final String HEADER_ADMIN_KEY = "Admin-Key";
    private static final String TEMPLATE_OBJECT_PREFIX = "template_object=";

    @Value("${kakao.api-url}")
    private String apiUrl;

    @Value("${kakao.admin-key}")
    private String adminKey;

    private final RestTemplate restTemplate;

    public KakaoMessageService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public void sendMessage(String accessToken, String message) {
        HttpHeaders headers = createHeaders(accessToken);
        String body = createRequestBody(message);

        HttpEntity<String> request = new HttpEntity<>(body, headers);

        ResponseEntity<String> response = restTemplate.postForEntity(apiUrl, request, String.class);

        if (response.getStatusCode().is2xxSuccessful()) {
            logger.info("Message sent successfully: " + response.getBody());
        } else {
            logger.error("Failed to send message: " + response.getStatusCode() + " - " + response.getBody());
        }
    }

    private HttpHeaders createHeaders(String accessToken) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        headers.set(HEADER_AUTHORIZATION, "Bearer " + accessToken);
        headers.set(HEADER_ADMIN_KEY, adminKey);
        return headers;
    }

    private String createRequestBody(String message) {
        return TEMPLATE_OBJECT_PREFIX + message;
    }
}
