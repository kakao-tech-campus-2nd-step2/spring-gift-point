package gift.product.integration;

import static org.assertj.core.api.Assertions.assertThat;

import gift.product.controller.KakaoController;
import gift.product.service.KakaoProperties;
import jakarta.validation.constraints.NotNull;
import java.net.URI;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.client.RestClient;

@SpringBootTest
public class KakaoTest {

    private final KakaoController kakaoController;
    private final RestClient client = RestClient.builder().build();

    @Autowired
    private KakaoProperties properties;

    @Autowired
    public KakaoTest(KakaoController kakaoController) {
        this.kakaoController = kakaoController;
    }

    @Test
    void testProperties() {
        assertThat(properties.clientId()).isNotEmpty();
        assertThat(properties.redirectUrl()).isEqualTo("http://localhost:8080");
    }

    @Test
    void testAuthRequest() {
        assertThat(kakaoController.login()).isEqualTo(
            "redirect:https://kauth.kakao.com/oauth/authorize?scope=talk_message&response_type=code"
                + "&redirect_uri=" + properties.redirectUrl()
                + "&client_id=" + properties.clientId()
        );
    }

    @Disabled
    @Test
    void testAccessTokenRequest() {
        var url = "https://kauth.kakao.com/oauth/token";
        final var body = createBody();
        var response = client.post()
            .uri(URI.create(url))
            .contentType(MediaType.APPLICATION_FORM_URLENCODED)
            .body(body)
            .retrieve()
            .toEntity(String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        System.out.println(response);
    }

    private @NotNull LinkedMultiValueMap<String, String> createBody() {
        var code = "kakao api로부터 받은 사용되지 않은 코드를 입력하여 테스트 합니다.";
        var body = new LinkedMultiValueMap<String, String>();
        body.add("grant_type", "authorization_code");
        body.add("client_id", properties.clientId());
        body.add("redirect_uri", properties.redirectUrl());
        body.add("code", code);
        return body;
    }
}
