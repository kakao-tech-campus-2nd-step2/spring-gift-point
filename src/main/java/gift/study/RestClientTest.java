package gift.study;

import static org.assertj.core.api.Assertions.*;
import jakarta.validation.constraints.NotNull;
import java.net.URI;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.client.RestClient;

@SpringBootTest
public class RestClientTest {
    @Autowired
    RestClient client;

    @Autowired
    private KakaoProperties properties;

    @Test
    void test2(){
        assertThat(properties.getClientId()).isNotEmpty();
        assertThat(properties.getRedirectUri()).isNotEmpty();
        System.out.println(properties);
    }

    @Test
    void test1(){
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

    private @NotNull LinkedMultiValueMap<String, String> createBody(){
        var body = new LinkedMultiValueMap<String, String>();
        var code = "YN6BIII8dJFutrj35VGbm0RbQPwUkqoDnEBksvUoM0SKK7GsYXyqZwAAAAQKPXRoAAABkOh60uPkNSpXBP-m7Q";
        body.add("grant_type", "authorization_code");
        body.add("client_id", properties.getClientId());
        body.add("redirect_uri", properties.getRedirectUri());
        body.add("code", code);
        return body;
    }
}
