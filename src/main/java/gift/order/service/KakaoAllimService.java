package gift.order.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

@Service
public class KakaoAllimService {
    @Value("${kakao.allim.uri}")
    private String allimUrl;
    private final RestClient restClient;

    public KakaoAllimService(RestClient restClient) {
        this.restClient = restClient;
    }

    public void sendAllim(String accessToken, String templateObject) {
        restClient.post()
                .uri(allimUrl)
                .header("Authorization", "Bearer " + accessToken)
                .body(templateObject)
                .retrieve()
                .body(String.class);
    }
}
