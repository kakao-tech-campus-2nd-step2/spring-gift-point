package gift.study;

import gift.common.config.KakaoProperties;
import jakarta.validation.constraints.NotNull;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.*;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.client.RestClient;

import java.net.URI;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class RestClientTest {
    private final RestClient client = RestClient.builder().build();

    @Autowired
    private KakaoProperties properties;

    @Test
    void test1() {
        var url = "https://kauth.kakao.com/oauth/token";

        // 바디 등록
        LinkedMultiValueMap<String, String> body = createBody();

        // 응답
        var response = client.post()
                .uri(URI.create(url))
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(body)
                .retrieve()
                .toEntity(String.class);

        // then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        System.out.println(response);
    }

    private @NotNull LinkedMultiValueMap<String, String> createBody() {
        var body = new LinkedMultiValueMap<String, String>();
        body.add("grant_type", properties.grantType()); // authorization_code로 고정
        body.add("client_id", properties.clientId()); // REST API 키
        body.add("redirect_uri", properties.redirectUri()); // 인가 코드가 리다이렉트된 URI
//        body.add("code", properties.authorizationCode()); // 인가 코드

        return body;
    }
}
