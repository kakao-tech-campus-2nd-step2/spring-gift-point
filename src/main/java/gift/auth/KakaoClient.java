package gift.auth;

import gift.order.KakaoMessage;
import java.net.URI;
import java.util.Objects;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.client.RestClient;

@Component
public class KakaoClient {

    @Value("${kakao.auth.token.url}")
    private String kakaoTokenUrl;

    @Value("${kakao.user.api.url}")
    private String kakaoUserApiUrl;

    @Value("${kakao.message-url")
    private String kakaoMessageUrl;

    private final RestClient restClient;
    private final KakaoProperties kakaoProperties;

    public KakaoClient(KakaoProperties kakaoProperties) {
        this.restClient = RestClient.builder().build();
        this.kakaoProperties = kakaoProperties;
    }

    public String getAccessToken(String authorizationCode) {
        LinkedMultiValueMap<String, String> body = kakaoProperties.createBody(authorizationCode);

        var response = restClient.post()
            .uri(URI.create(kakaoTokenUrl))
            .contentType(MediaType.APPLICATION_FORM_URLENCODED)
            .body(body)
            .retrieve()
            .toEntity(KakaoAccessToken.class);

        return Objects.requireNonNull(response.getBody()).accessToken();
    }

    public KakaoUserInfo getUserInfo(String accessToken) {
        return restClient.get()
            .uri(kakaoUserApiUrl)
            .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken)
            .accept(MediaType.APPLICATION_JSON)
            .retrieve()
            .toEntity(KakaoUserInfo.class)
            .getBody();
    }

    public void sendMessage(String accessToken, String message) {
        var body = KakaoMessage.createBody(message);

        restClient.post()
            .uri(URI.create(kakaoMessageUrl))
            .contentType(MediaType.APPLICATION_FORM_URLENCODED)
            .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken)
            .body(body)
            .retrieve()
            .toEntity(String.class);
    }

}
