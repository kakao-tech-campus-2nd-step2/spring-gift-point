package gift;

import static org.assertj.core.api.Assertions.assertThat;

import gift.config.KakaoProperties;
import gift.dto.KakaoTokenResponse;
import io.github.cdimascio.dotenv.Dotenv;
import jakarta.validation.constraints.NotNull;
import java.net.URI;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.client.RestTemplate;

@SpringBootTest
public class RestTemplateTest {

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private KakaoProperties properties;

    private static Dotenv dotenv;
    private static String authorizationCode;

    @BeforeAll
    public static void setup() {
        dotenv = Dotenv.load();
        authorizationCode = dotenv.get("AUTHORIZATION_CODE");
    }

    @Test
    @Disabled("외부 API 호출 테스트로, 필요시 수동으로 실행하세요.")
    void testKakaoProperties() {
        assertThat(properties.getClientId()).isNotEmpty();
        assertThat(properties.getRedirectUri()).isNotEmpty();
        assertThat(properties.getClientSecret()).isNotEmpty();
    }

    @Test
    @Disabled("외부 API 호출 테스트로, 필요시 수동으로 실행하세요.")
    void testGetAccessToken() {
        var url = "https://kauth.kakao.com/oauth/token";
        var headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_FORM_URLENCODED_VALUE);

        var body = createBody();

        var request = new RequestEntity<>(body, headers, HttpMethod.POST, URI.create(url));

        var response = restTemplate.exchange(url, HttpMethod.POST, request, KakaoTokenResponse.class);

        assertThat(response.getStatusCode().is2xxSuccessful()).isTrue();
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getAccessToken()).isNotEmpty();
    }

    private @NotNull LinkedMultiValueMap<String, String> createBody() {
        var body = new LinkedMultiValueMap<String, String>();
        body.add("grant_type", "authorization_code");
        body.add("client_id", properties.getClientId());
        body.add("redirect_uri", properties.getRedirectUri());
        body.add("code", authorizationCode);
        body.add("client_secret", properties.getClientSecret());
        return body;
    }
}
