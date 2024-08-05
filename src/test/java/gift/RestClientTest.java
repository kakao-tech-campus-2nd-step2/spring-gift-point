package gift;

import gift.common.properties.KakaoProperties;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.client.RestClient;

import java.net.URI;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class RestClientTest {
    @Autowired
    private RestClient restClient;

    @Autowired
    private KakaoProperties kakaoProperties;

    @Test
    @DisplayName("액세스 토큰을 잘 받아오는지 테스트")
    void testGetAccessToken() {
        var url = "https://kauth.kakao.com/oauth/token";
        var body = new LinkedMultiValueMap<String, String>();
        body.add("grant_type", "authorization_code");
        body.add("client_id", kakaoProperties.restAPiKey());
        body.add("redirect_url", "http://localhost:8080");
        body.add("code", "ZjX1dBYc4mQufxfZKzyYdkDPGwvvUB7Tui91ZafEaCnEJEYyQxkZaQAAAAQKPXNNAAABkN8ZKY-t1856Xp2T3g");
        var response = restClient.post()
                .uri(URI.create(url))
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(body)
                .retrieve()
                .toEntity(String.class); // response
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

        System.out.println(response);
    }
}