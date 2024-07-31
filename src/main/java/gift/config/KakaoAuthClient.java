package gift.config;

import gift.dto.KakaoTokenResponseDto;
import gift.model.member.KakaoProperties;
import io.netty.handler.codec.http.HttpHeaderValues;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Component
public class KakaoAuthClient {
    private final WebClient webClient;
    private final KakaoProperties kakaoProperties;

    public KakaoAuthClient(WebClient.Builder webClientBuilder, KakaoProperties kakaoProperties) {
        this.kakaoProperties = kakaoProperties;
        this.webClient = webClientBuilder
                .baseUrl(kakaoProperties.getKakaoAuthUrl())
                .build();
    }

    public Mono<KakaoTokenResponseDto> getAccessToken(String code) {
        return webClient.post()
                .uri(uriBuilder -> uriBuilder
                        .scheme("https")
                        .path("/oauth/token")
                        .queryParam("grant_type", "authorization_code")
                        .queryParam("client_id", kakaoProperties.getClientId())
                        .queryParam("code", code)
                        .build(true))
                .header(HttpHeaders.CONTENT_TYPE, HttpHeaderValues.APPLICATION_X_WWW_FORM_URLENCODED.toString())
                .retrieve()
                .onStatus(HttpStatusCode::isError, clientResponse -> handleErrorResponse(clientResponse.statusCode()))
                .bodyToMono(KakaoTokenResponseDto.class);
    }

    public void kakaoDisconnect(String accessToken) {
        webClient.post()
                .uri("/v1/user/logout")
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken)
                .header(HttpHeaders.CONTENT_TYPE, "application/x-www-form-urlencoded")
                .retrieve()
                //TODO : Custom Exception
                .onStatus(HttpStatusCode::isError, clientResponse -> handleErrorResponse(clientResponse.statusCode()));
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