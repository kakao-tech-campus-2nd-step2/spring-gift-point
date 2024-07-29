package gift;

import static org.assertj.core.api.Assertions.assertThat;

import java.net.URI;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.client.RestClient;

public class RestClientTest {

    private final RestClient client = RestClient.builder().build();


    void test1() {
        var url = "https://kauth.kakao.com/oauth/token";
        var body = new LinkedMultiValueMap<String, String>();
        body.add("grant_type", "authorization_code");
        body.add("client_id", "1cd2fb0355dd3eedd87de95056a4a4a5");
        body.add("redirect_uri", "http://localhost:8080");
        body.add("code",
            "U4aIXiGc7sj133wOCwahTVa8kv2oVRdBb3RZrd7-QzvwsexV7U8arQAAAAQKPCPoAAABkNg86-wWphHJzwXJqw");

        var response = client.post()
            .uri(URI.create(url))
            .contentType(MediaType.APPLICATION_FORM_URLENCODED)
            .body(body)
            .retrieve()
            .toEntity(String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        System.out.println(response);
    }
}