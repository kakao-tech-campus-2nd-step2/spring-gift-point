package gift.user.oauth;

import java.net.URI;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.MediaType;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestTemplate;

@SpringBootTest
public class RestTest {
    private final RestTemplate client1 = new RestTemplateBuilder().build();
    private final RestClient client2 = RestClient.builder().build();

    @Autowired
    private KakaoProperties kakaoProperties;

    @Test
    void test1() {
        var url = "https://kauth.kakao.com/oauth/token";
        var body = new LinkedMultiValueMap<String, String>();
        body.add("grant_type", "authorization_code");
        body.add("client_id", kakaoProperties.clientId());
        body.add("redirect_url", kakaoProperties.redirectUri());
        body.add("code", "_FWxXm_dIPb8xhouv_8e6fTI_H_1-3EBVL9WJ1WyUWieYr3KdvK9zAAAAAQKKiVQAAABkOBvLNohI_W2iNNaeg");
        var response = client2.post()
                .uri(URI.create(url))
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(body)
                .retrieve()
                .toEntity(String.class);

        System.out.println(response);
    }
}