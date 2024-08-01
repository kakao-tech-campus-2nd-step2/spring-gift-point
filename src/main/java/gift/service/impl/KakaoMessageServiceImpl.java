package gift.service.impl;

import gift.service.KakaoMessageService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
public class KakaoMessageServiceImpl implements KakaoMessageService {

    private final WebClient webClient;

    public KakaoMessageServiceImpl() {
        this.webClient = WebClient.builder()
            .baseUrl("https://kapi.kakao.com")
            .build();
    }

    @Override
    public Mono<ResponseEntity<String>> sendMessage(String accessToken, String message) {
        HttpHeaders headers = createHeaders(accessToken);
        MultiValueMap<String, String> body = createMessageBody(message);

        return webClient.post()
            .uri("/v2/api/talk/memo/default/send")
            .headers(httpHeaders -> httpHeaders.addAll(headers))
            .bodyValue(body)
            .retrieve()
            .toEntity(String.class);
    }

    public HttpHeaders createHeaders(String accessToken) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", accessToken);
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        return headers;
    }

    public MultiValueMap<String, String> createMessageBody(String message) {
        String templateObject = "{"
            + "\"object_type\": \"text\","
            + "\"text\": \"" + message + "\","
            + "\"link\": {"
            + "  \"web_url\": \"https://developers.kakao.com\","
            + "  \"mobile_web_url\": \"https://developers.kakao.com\""
            + "},"
            + "\"button_title\": \"바로 확인\""
            + "}";

        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("template_object", templateObject);
        return body;
    }
}
