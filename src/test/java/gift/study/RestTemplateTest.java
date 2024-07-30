package gift.study;

import jakarta.validation.constraints.NotNull;
import java.net.URI;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.client.RestTemplate;

@ConfigurationPropertiesScan
@ConfigurationProperties
record KakaoProperties(
    String clientId,
    String redirectUrl
){}
@SpringBootTest
class RestTemplateTest {
    @Autowired
    private KakaoProperties properties;
    private final RestTemplate client = new RestTemplateBuilder().build();

    @Test
    void test2() {
        Assertions.assertThat(properties.clientId()).isNotEmpty();
        Assertions.assertThat(properties.redirectUrl()).isNotEmpty();
        System.out.println(properties);
    }
    @Test
    void test1() {
        var url = "https://kauth.kakao.com/oauth/token";
        var header = new HttpHeaders();
        header.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_FORM_URLENCODED_VALUE);
        final var body = createBody();

        var request = new RequestEntity<>(body, header, HttpMethod.POST, URI.create(url));
        var response = client.exchange(request, String.class);
        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        System.out.println(response);
    }

    private static @NotNull LinkedMultiValueMap<String, String> createBody() {
        var code = "9gGBlfvAdB_Ac1xt4VUBHcAW58Ucb22bWXeSSd7xUaUH6uhXk227lwAAAAQKKiUPAAABkNg8ylSUJG13ldIf8A";
        var properties = new KakaoProperties("8d3af979955ed265f35f4a53e03c4090", "http://localhost:8080");
        var body = new LinkedMultiValueMap<String, String>();
        body.add("grant_type", "authorization_code");
        body.add("client_id", properties.clientId());
        body.add("redirect_url", properties.redirectUrl());
        body.add("code", code);
        return body;
    }
}
