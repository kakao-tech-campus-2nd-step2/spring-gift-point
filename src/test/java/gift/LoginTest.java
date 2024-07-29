package gift;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.*;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.net.URI;

public class LoginTest {
    @Value("${api.login.url}")
    private String apiLoginUrl;

    @Value("${redirect.url}")
    private String redirectUrl;

    @Value("${rest.secretKey}")
    private String apiClientId;

    @Value("${authorization.code}")
    private String authorizationCode;

    private final RestTemplate client = new RestTemplateBuilder().build();

    @Test
    void apiLoginTest() {
        var url = "https://kauth.kakao.com/oauth/token";
        var headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_FORM_URLENCODED_VALUE);

        var body = new LinkedMultiValueMap<String, String>();
        body.add("grant_type", "authorization_code");
        body.add("client_id", apiClientId);
        body.add("redirect_url", redirectUrl);
        body.add("code", authorizationCode);

        var request = new RequestEntity<>(body, headers, HttpMethod.POST, URI.create(url));
        var response = client.exchange(request, String.class);
        System.out.println(response);
    }
}
