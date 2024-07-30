package gift.api.kakaoMessage;

import gift.api.KakaoProperties;
import gift.api.aop.TokenRefresher;
import gift.order.Order;
import java.net.URI;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Component
public class KakaoMessageClient {

    private final KakaoProperties kakaoProperties;
    private final KakaoMessageMaker kakaoMessageMaker;
    private final RestClient restClient;

    public KakaoMessageClient(KakaoProperties kakaoProperties, KakaoMessageMaker kakaoMessageMaker,
        RestClient restClient) {
        this.kakaoProperties = kakaoProperties;
        this.kakaoMessageMaker = kakaoMessageMaker;
        this.restClient = restClient;
    }

    @TokenRefresher
    @Retryable(backoff = @Backoff(delay = 1000))
    public void sendOrderMessage(String accessToken, Order order) {
        restClient.post()
            .uri(URI.create(kakaoProperties.messageUrl()))
            .header("Authorization", "Bearer " + accessToken)
            .body(kakaoMessageMaker.createOrderMessage(order))
            .retrieve();
    }

}
