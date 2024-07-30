package gift.study;

import jakarta.validation.constraints.NotNull;
import java.net.URI;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.client.RestClient;

public class RestClientTest {
    private final RestClient client = RestClient.builder().build();

    @Test
    void test1() {
        var url = "https://kauth.kakao.com/oauth/token";
        final var body = createBody();
        var response = client.post()
            .uri(URI.create(url))
            .contentType(MediaType.APPLICATION_FORM_URLENCODED)
            .body(body)
            .retrieve()
            .toEntity(String.class);
        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        System.out.println(response);
    }

    private static @NotNull LinkedMultiValueMap<String, String> createBody() {
        var body = new LinkedMultiValueMap<String, String>();
        body.add("grant_type", "authorization_code");
        body.add("client_id", "8d3af979955ed265f35f4a53e03c4090");
        body.add("redirect_url", "http://localhost:8080");
        body.add("code", "9gGBlfvAdB_Ac1xt4VUBHcAW58Ucb22bWXeSSd7xUaUH6uhXk227lwAAAAQKKiUPAAABkNg8ylSUJG13ldIf8A");
        return body;
    }

}
