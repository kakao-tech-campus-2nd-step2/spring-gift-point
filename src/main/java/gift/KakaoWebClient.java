package gift;

import gift.dto.OrderDTO;
import gift.service.KakaoMessageService;
import io.netty.channel.ChannelOption;
import io.netty.handler.timeout.ReadTimeoutHandler;
import io.netty.handler.timeout.WriteTimeoutHandler;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.TimeUnit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;

import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.BodyInserters;

import com.fasterxml.jackson.databind.ObjectMapper;

import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.Map;

@Component
public class KakaoWebClient {
    private static final Logger logger = LoggerFactory.getLogger(KakaoMessageService.class);
    private final WebClient webClient;
    private final ObjectMapper objectMapper;
    private final KakaoProperties kakaoProperties;

    public KakaoWebClient(KakaoProperties kakaoProperties, ObjectMapper objectMapper) {
        this.kakaoProperties = kakaoProperties;
        this.objectMapper = objectMapper;
        this.webClient = WebClient.builder()
            .baseUrl(kakaoProperties.getBaseUrl())
            .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_FORM_URLENCODED_VALUE)
            .clientConnector(new ReactorClientHttpConnector(
                HttpClient.create()
                    .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 5000)
                    .doOnConnected(conn -> conn
                        .addHandlerLast(new ReadTimeoutHandler(5000, TimeUnit.MILLISECONDS))
                        .addHandlerLast(new WriteTimeoutHandler(5000, TimeUnit.MILLISECONDS))
                    )
            ))
            .build();
    }

    public WebClient getWebClient() {
        return webClient;
    }

    public String getAccessToken(String authorizationCode) {
        String url = "/oauth/token";
        return webClient.post()
            .uri(url)
            .body(BodyInserters.fromFormData("grant_type", "authorization_code")
                .with("client_id", kakaoProperties.getClientId())
                .with("redirect_uri", kakaoProperties.getRedirectUri())
                .with("code", authorizationCode))
            .retrieve()
            .bodyToMono(String.class)
            .block();
    }

    public String getUserInfo(String accessToken) {
        String url = kakaoProperties.getUserInfoUrl();
        return webClient.get()
            .uri(url)
            .headers(headers -> headers.setBearerAuth(accessToken))
            .retrieve()
            .bodyToMono(String.class)
            .block();
    }

    public boolean send(String accessToken, OrderDTO orderDTO, String productName, String optionName) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + accessToken);

        LocalDateTime orderDateTime = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String formattedDateTime = orderDateTime.format(formatter);

        String messageContent = String.format(
            "Order Details:\nProduct: %s\nOption: %s\nQuantity: %d\nMessage: %s\nOrder DateTime: %s",
            productName, optionName, orderDTO.getQuantity(), orderDTO.getMessage(), formattedDateTime
        );

        try {
            MultiValueMap<String, String> parameters = new LinkedMultiValueMap<>();
            parameters.add("template_object", objectMapper.writeValueAsString(createMessagePayload(messageContent)));

            return webClient.post()
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