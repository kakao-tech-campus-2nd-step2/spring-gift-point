package gift;

import static org.assertj.core.api.Assertions.assertThat;

import java.net.URI;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.client.RestTemplate;

@EnableConfigurationProperties
record KakaoProperties(
    String clientId,
    String redirectUrl
){}

public class RestTemplateTest {
    private final RestTemplate client = new RestTemplateBuilder().build();

    @Autowired
    private KakaoProperties properties;


    void test1() {
        var url = "https://kauth.kakao.com/oauth/token";
        var headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_FORM_URLENCODED_VALUE);
        var body = new LinkedMultiValueMap<String, String>();
        body.add("grant_type", "authorization_code");
        body.add("client_id", "1cd2fb0355dd3eedd87de95056a4a4a5");
        body.add("redirect_uri", "http://localhost:8080");
        body.add("code", "7fwBwuogxQPoxCffjJ5Kav4s1TcxdaPuolHc2yF5Q2Esu5_WsvjEUgAAAAQKKiUQAAABkNgsFjhtZc76WqiBKA");
        var request = new RequestEntity<>(body, headers, HttpMethod.POST, URI.create(url));
        var response = client.exchange(request, String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        System.out.println(response);
    }
}