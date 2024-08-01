package gift;

import java.net.URI;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.client.RestClient;

record KakaoProperties(
    String clientId,
    String redirectUrl
) {}

public class RestClientTest {
    private final RestClient client = RestClient.builder().build();

    @Test
    void test1() {
        var url = "https://kauth.kakao.com/oauth/token";
        var body = createBody();
        var response = client.post()
            .uri(URI.create(url))
            .contentType(MediaType.APPLICATION_FORM_URLENCODED)
            .body(body) //request
            .retrieve()
            .toEntity(String.class); //response

        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        System.out.println(response);
    }

    private static LinkedMultiValueMap<String, String> createBody() {
        var code = "5IxLaYM_FlRRXxgJMCqkYKcWVYyHlRiYR1HeFbnN7pBw9eu_AoskTgAAAAQKKcjaAAABkO75hJGi-KZYUq23DA";
        var properties = new KakaoProperties("a59e14e5fdae98b3575664e4f567e141","http://localhost:8080/login/kakao-callback");
        var body = new LinkedMultiValueMap<String, String>();
        body.add("grant_type", "authorization_code");
        body.add("client_id", properties.clientId());
        body.add("redirect_uri", properties.redirectUrl());
        body.add("code", code);
        return body;
    }
}
