package gift.order.infrastructure.gateway;

import gift.core.domain.authentication.exception.AuthenticationFailedException;
import gift.order.service.OrderAlarmGateway;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClient;

@Component
public class KakaoOrderAlarmGateway implements OrderAlarmGateway {
    @Value("${app.order.gateway.kakao}")
    private String kakaoUrl;

    private final RestClient restClient;

    public KakaoOrderAlarmGateway(RestClient restClient) {
        this.restClient = restClient;
    }

    public void sendAlarm(String accessToken, String message) {
        restClient
                .post()
                .uri(kakaoUrl)
                .headers(headers -> {
                    headers.setBearerAuth(accessToken);
                    headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
                })
                .body(buildParams(message))
                .retrieve()
                .onStatus(HttpStatusCode::isError, ((request, response) -> {
                    throw new AuthenticationFailedException();
                }))
                .body(String.class);
    }

    private MultiValueMap<String, String> buildParams(String message) {
        LinkedMultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("template_object", message);
        return params;
    }
}
