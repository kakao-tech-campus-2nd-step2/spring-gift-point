package gift.study;

import static org.assertj.core.api.Assertions.assertThat;

import java.net.URI;
import org.junit.jupiter.api.Test;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.client.RestTemplate;

public class RestTemplateTest {

    private final RestTemplate client = new RestTemplateBuilder().build();

    @Test
    void test1() {
        var url = "https://kauth.kakao.com/oauth/token";
        var headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_FORM_URLENCODED_VALUE);
        var body = createBody();
        var request = new RequestEntity<>(body, headers, HttpMethod.POST, URI.create(url));
        var response = client.exchange(request, String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        System.out.println(response);
    }

    private static LinkedMultiValueMap<String, String> createBody() {
        var body = new LinkedMultiValueMap<String, String>();
        body.add("grant_type", "authorization_code");
        body.add("client_id", "6f206d72daae46a758db23f85f2f2ba6");
        body.add("redirect_url", "http://localhost:8080");
        body.add("code", "g92H6Ml6RSEnwIIcsCLyB9NY30_UXJmXGr4KNjloIP_J2MmNDJMRrAAAAAQKPXKYAAABkNgpOONtZc76WqiBKA");
        return body;
    }
}
