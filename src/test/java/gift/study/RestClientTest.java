package gift.study;

import static org.assertj.core.api.Assertions.assertThat;

import java.net.URI;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.client.RestClient;

@SpringBootTest
public class RestClientTest {

    private final RestClient client = RestClient.builder().build();

    @Test
    void test1() {
        var url = "https://kauth.kakao.com/oauth/token";
        var body = createBody();
        var response = client.post()
            .uri(URI.create(url))
            .contentType(MediaType.APPLICATION_FORM_URLENCODED)
            .body(body)
            .retrieve()
            .toEntity(String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        System.out.println(response);
    }

    private static LinkedMultiValueMap<String, String> createBody() {
        var body = new LinkedMultiValueMap<String, String>();
        body.add("grant_type", "authorization_code");
        body.add("client_id", "6f206d72daae46a758db23f85f2f2ba6");
        body.add("redirect_url", "http://localhost:8080");
        body.add("code", "g8MZooFGxCcW5MAprdgKNfUKpjlyKkq7oBZuyKVptI4YWw_V9e6IcgAAAAQKPCQfAAABkNg8QCKQgW3aWXatGQ");
        return body;
    }
}
