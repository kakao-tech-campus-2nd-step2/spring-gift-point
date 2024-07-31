package gift.study;

import jakarta.validation.constraints.NotNull;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.*;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestTemplate;

import java.net.URI;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@ConfigurationPropertiesScan
@ConfigurationProperties("kakao")
record KakaoProperties(
    String clientId,
    String redirectUrl
) {}

@ActiveProfiles("test")
@SpringBootTest
public class RestTemplateTest {
    private final RestClient client = RestClient.builder().build();

    @Autowired
    private KakaoProperties properties;

    @Test
    void test2() {
        assertThat(properties.clientId()).isNotEmpty();
        assertThat(properties.redirectUrl()).isNotEmpty();
        assertThat(properties.redirectUrl()).isEqualTo("http://localhost:8080");
    }

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
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        System.out.println(response);
    }

    private static @NotNull LinkedMultiValueMap<String, String> createBody() {
        var code = "";
    }
}
