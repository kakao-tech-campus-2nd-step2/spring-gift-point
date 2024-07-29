package gift.Product;

import org.junit.jupiter.api.Test;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.*;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestTemplate;

import java.net.URI;

import static org.assertj.core.api.Assertions.assertThat;

public class RestTemplelateTest {
    private final RestTemplate client1 = new RestTemplateBuilder().build();
    private final RestClient client2 = RestClient.builder().build();

    @Test
    void test1() {
        var url = "https://kauth.kakao.com/oauth/token";
        var headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_FORM_URLENCODED_VALUE);
        var body = new LinkedMultiValueMap<String, String>();
        body.add("grant_type", "authorization_code");
        body.add("client_id", "8b0993ea8425d3f401667223d8d6b1a7");
        body.add("redirect_url", "http://localhost:8080");
        body.add("code", "HIUVpitVIkkmEoLDZ5WASUYIzYFm4pMU5U5zk_rUbW9FDPNCIft9SgAAAAQKPXLrAAABkOJEMHVSGUcvaFb1Eg");
        var request = new RequestEntity<>(body, headers, HttpMethod.POST, URI.create(url));
        var response = client1.exchange(request, String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        System.out.println(response);
    }

    @Test
    void test2() {
        var url = "https://kauth.kakao.com/oauth/token";
        var body = new LinkedMultiValueMap<String, String>();
        body.add("grant_type", "authorization_code");
        body.add("client_id", "8b0993ea8425d3f401667223d8d6b1a7");
        body.add("redirect_url", "http://localhost:8080");
        body.add("code", "HIUVpitVIkkmEoLDZ5WASUYIzYFm4pMU5U5zk_rUbW9FDPNCIft9SgAAAAQKPXLrAAABkOJEMHVSGUcvaFb1Eg");
        var response = client2.post()
            .uri(URI.create(url))
            .contentType(MediaType.APPLICATION_FORM_URLENCODED)
            .body(body)
            .retrieve()
            .toEntity(String.class);

        System.out.println(response);
    }
}

