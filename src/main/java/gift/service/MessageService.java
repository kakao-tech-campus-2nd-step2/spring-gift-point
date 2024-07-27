package gift.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import gift.dto.MessageTemplate;
import gift.exception.CustomException.GenericException;
import gift.exception.CustomException.ValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class MessageService {

    @Value("${kakao-admin-key}")
    private String adminKey;

    @Value("${kakao-api-url}")
    private String apiUrl;

    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    @Autowired
    public MessageService(RestTemplate restTemplate, ObjectMapper objectMapper) {
        this.restTemplate = restTemplate;
        this.objectMapper = objectMapper;
    }

    public String sendMessage(String recipient) throws GenericException {
        try {
            MessageTemplate template = new MessageTemplate.Builder("Hello, " + recipient + "!").build();
            String requestBody = objectMapper.writeValueAsString(template);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.setBearerAuth(adminKey);

            HttpEntity<String> entity = new HttpEntity<>(requestBody, headers);

            ResponseEntity<String> response = restTemplate.postForEntity(apiUrl + "/v2/api/talk/memo/default/send", entity, String.class);

            if (response.getStatusCode() != HttpStatus.OK) {
                throw new ValidationException("Failed to send message: " + response.getStatusCode());
            }
            return response.getBody();
        } catch (Exception e) {
            throw new GenericException("Failed to send message: " + e.getMessage(), e);
        }
    }
}
