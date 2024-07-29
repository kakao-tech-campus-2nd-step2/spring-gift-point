package gift.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import gift.KakaoProperties;
import gift.api.OrderRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

@Service
public class KakaoMessageService {

    private static final Logger logger = LoggerFactory.getLogger(KakaoMessageService.class);
    private final WebClient kakaoWebClient;
    private final KakaoProperties kakaoProperties;
    private final ObjectMapper objectMapper;
    private final OptionService optionService;
    private final ProductService productService;

    public KakaoMessageService(WebClient kakaoWebClient, KakaoProperties kakaoProperties, ObjectMapper objectMapper, OptionService optionService, ProductService productService) {
        this.kakaoWebClient = kakaoWebClient;
        this.kakaoProperties = kakaoProperties;
        this.objectMapper = objectMapper;
        this.optionService = optionService;
        this.productService = productService;
    }

    public boolean sendKakaoMessage(String accessToken, OrderRequest orderRequest) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        headers.set("Authorization", "Bearer " + accessToken);

        String productName = productService.getProductNameById(orderRequest.getProductId());
        String optionName = optionService.getOptionNameById(orderRequest.getOptionId());
        LocalDateTime orderDateTime = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String formattedDateTime = orderDateTime.format(formatter);

        String messageContent = String.format(
            "Order Details:\nProduct: %s\nOption: %s\nQuantity: %d\nMessage: %s\nOrder DateTime: %s",
            productName, optionName, orderRequest.getQuantity(), orderRequest.getMessage(), formattedDateTime
        );

        try {
            MultiValueMap<String, String> parameters = new LinkedMultiValueMap<>();
            parameters.add("template_object", objectMapper.writeValueAsString(createMessagePayload(messageContent)));

            return kakaoWebClient.post()
                .uri(kakaoProperties.getSendMessageUrl())
                .headers(httpHeaders -> httpHeaders.addAll(headers))
                .body(BodyInserters.fromFormData(parameters))
                .retrieve()
                .onStatus(status -> status.isError(), response -> Mono.error(new RuntimeException("Error while sending Kakao message")))
                .bodyToMono(String.class)
                .map(response -> true)
                .onErrorReturn(false)
                .block();
        } catch (Exception e) {
            logger.error("Failed to send Kakao message", e);
            return false;
        }
    }

    private Map<String, Object> createMessagePayload(String messageContent) {
        Map<String, Object> payload = new HashMap<>();
        payload.put("object_type", "text");
        payload.put("text", messageContent);
        Map<String, String> link = new HashMap<>();
        link.put("web_url", "https://www.example.com");
        link.put("mobile_web_url", "https://www.example.com");
        payload.put("link", link);
        payload.put("button_title", "Open");
        return payload;
    }
}