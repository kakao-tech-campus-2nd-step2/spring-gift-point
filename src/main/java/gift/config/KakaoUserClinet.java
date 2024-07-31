package gift.config;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import gift.dto.KakaoInfoDto;
import gift.model.member.KakaoProperties;
import io.netty.handler.codec.http.HttpHeaderValues;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Component
public class KakaoUserClinet {
    private final WebClient webClient;

    public KakaoUserClinet(WebClient.Builder webClientBuilder, KakaoProperties kakaoProperties) {
        this.webClient = webClientBuilder
                .baseUrl(kakaoProperties.getUserInfoUrl())
                .build();
    }

    public KakaoInfoDto getUserInfo(String accessToken) throws JsonProcessingException {
        String responseBody = webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .scheme("https")
                        .path("/v2/user/me")
                        .build(true))
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken)
                .header(HttpHeaders.CONTENT_TYPE, HttpHeaderValues.APPLICATION_X_WWW_FORM_URLENCODED.toString())
                .retrieve()
                //TODO : Custom Exception
                .onStatus(HttpStatusCode::isError, clientResponse -> handleErrorResponse(clientResponse.statusCode()))
                .bodyToMono(String.class)
                .block();

        ObjectMapper objectMapper = new ObjectMapper();
        KakaoInfoDto kakaoInfoDto = objectMapper.readValue(responseBody, KakaoInfoDto.class);
        return kakaoInfoDto;
    }

    public Mono<Integer> sendOrderConfirmationMessage(String message, String accessToken) {
        return webClient.post()
                .uri(uriBuilder -> uriBuilder
                        .path("/v2/api/talk/memo/default/send")
                        .build())
                .header("Authorization", "Bearer " + accessToken)
                .header("Content-Type", "application/x-www-form-urlencoded")
                .bodyValue("template_object=" + message)
                .retrieve()
                .onStatus(HttpStatusCode::isError, clientResponse -> handleErrorResponse(clientResponse.statusCode()))
                .bodyToMono(Integer.class);
    }

    private Mono<Throwable> handleErrorResponse(HttpStatusCode statusCode) {
        if (statusCode.is4xxClientError()) {
            return Mono.error(new RuntimeException("Invalid Parameter"));
        }
        if (statusCode.is5xxServerError()) {
            return Mono.error(new RuntimeException("Internal Server Error"));
        }
        return Mono.error(new RuntimeException("Unexpected Error"));
    }
}
