package gift.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import gift.exception.kakao.KakaoMessageException;
import gift.exception.oauth2.OAuth2TokenException;
import gift.model.OAuth2AccessToken;
import gift.repository.OAuth2AccessTokenRepository;
import gift.request.kakaomessage.KakaoLink;
import gift.response.KakaoMessageToMeResponse;
import java.net.URI;
import java.time.Duration;
import java.util.HashMap;
import java.util.Map;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;

@Service
public class KakaoMessageService {

    @Value("${kakao.message-to-me-request-uri}")
    public String MESSAGE_REQUEST_TO_ME_URI;

    private final WebClient client;
    private final OAuth2AccessTokenRepository accessTokenRepository;
    private final ObjectMapper objectMapper;

    public KakaoMessageService(WebClient client,
        OAuth2AccessTokenRepository accessTokenRepository, ObjectMapper objectMapper) {
        this.client = client;
        this.accessTokenRepository = accessTokenRepository;
        this.objectMapper = objectMapper;
    }

    public void sendMessageToMe(Long memberId, String message) {

        OAuth2AccessToken oAuth2AccessToken = accessTokenRepository.findByMemberId(memberId)
            .orElseThrow(() -> new OAuth2TokenException("토큰이 존재하지 않습니다."));

        KakaoLink link = KakaoLink.createLink();

        try {
            KakaoMessageToMeResponse response = client.post()
                .uri(URI.create(MESSAGE_REQUEST_TO_ME_URI))
                .header("Authorization", "Bearer " + oAuth2AccessToken.getAccessToken())
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(Mono.just(createTextMessage(message, link)),
                    LinkedMultiValueMap.class)
                .retrieve()
                .bodyToMono(KakaoMessageToMeResponse.class)
                .timeout(Duration.ofMillis(500))
                .retry(3)
                .block();

            if (response == null || response.resultCode() != 0) {
                throw new KakaoMessageException();
            }

        } catch (WebClientResponseException | JsonProcessingException e) {
            throw new KakaoMessageException(e);
        }

    }


    public LinkedMultiValueMap<String, String> createTextMessage(String message, KakaoLink link)
        throws JsonProcessingException {
        LinkedMultiValueMap<String, String> body = new LinkedMultiValueMap<>();

        HashMap<String, Object> templateObject = new HashMap<>();
        templateObject.put("object_type", "text");
        templateObject.put("text", message);
        templateObject.put("link", link);

        body.add("template_object", objectMapper.writeValueAsString(templateObject));
        return body;
    }


}
