package gift.e2e;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.springframework.test.web.client.ExpectedCount.once;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withStatus;

import com.fasterxml.jackson.databind.ObjectMapper;
import gift.config.KakaoProperties;
import gift.dto.KakaoTokenResponse;
import gift.exception.KakaoNotFoundException;
import java.net.URI;
import java.util.Map;
import java.util.Objects;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.DefaultUriBuilderFactory;
import org.springframework.web.util.UriComponentsBuilder;

@SpringBootTest
@AutoConfigureMockMvc
class KakaoUserE2ETest {

    private final String testCode = "testCode";
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private KakaoProperties kakaoProperties;
    @Autowired
    private RestTemplate restTemplate;

    private MockRestServiceServer mockServer;

    @BeforeEach
    void setUp() {
        mockServer = MockRestServiceServer.createServer(restTemplate);
    }

    @Test
    @DisplayName("인가 코드 테스트")
    void authizationTest() {
        String baseUri = "/oauth/authorize";
        DefaultUriBuilderFactory uriBuilderFactory = new DefaultUriBuilderFactory(baseUri);
        String authCodeRequestUri = uriBuilderFactory.builder()
            .queryParam("response_type", "code")
            .queryParam("redirect_uri", kakaoProperties.getRedirectUri())
            .queryParam("client_id", kakaoProperties.getClientId())
            .build()
            .toString();

        mockServer.expect(requestTo(authCodeRequestUri))
            .andExpect(method(HttpMethod.GET))
            .andRespond(withStatus(HttpStatus.FOUND)
                .location(URI.create(kakaoProperties.getRedirectUri() + "?code=" + testCode)));

        ResponseEntity<String> response = getResponse(authCodeRequestUri);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.FOUND);
        Map<String, String> queryParams = UriComponentsBuilder.fromUri(
                Objects.requireNonNull(response.getHeaders().getLocation()))
            .build()
            .getQueryParams()
            .toSingleValueMap();
        assertThat(queryParams.get("code")).isEqualTo(testCode);
    }

    @Test
    @DisplayName("accessToken 테스트")
    void getAccessTokenTest() throws Exception {
        KakaoTokenResponse testResponse = new KakaoTokenResponse("testAccessToken", "bearer",
            "test", 21599, "test");
        String tokenRequestUri = "/oauth/token";

        mockServer.expect(requestTo(tokenRequestUri))
            .andExpect(method(HttpMethod.GET))
            .andRespond(withStatus(HttpStatus.OK)
                .body(objectMapper.writeValueAsString(testResponse))
                .contentType(MediaType.APPLICATION_JSON));

        ResponseEntity<String> response = getResponse(tokenRequestUri);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        KakaoTokenResponse tokenResponse = objectMapper.readValue(response.getBody(),
            KakaoTokenResponse.class);
        assertThat(tokenResponse.accessToken()).isEqualTo(testResponse.accessToken());
        assertThat(tokenResponse.accessToken()).isEqualTo("testAccessToken");
    }

    private ResponseEntity<String> getResponse(String url) {
        LinkedMultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("grant_type", "authorization_code");
        body.add("client_id", kakaoProperties.getClientId());
        body.add("redirect_uri", kakaoProperties.getRedirectUri());
        body.add("code", testCode);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        HttpEntity<LinkedMultiValueMap<String, String>> requestEntity = new HttpEntity<>(body,
            headers);

        return restTemplate.exchange(url, HttpMethod.GET,
            requestEntity, String.class);
    }

    @Test
    @DisplayName("404 Not Found 에러 테스트")
    void KakaoNotFoundExceptionTest() {
        mockServer.expect(once(), requestTo("https://kauth.kakao.com/oauth/token?code=" + testCode))
            .andExpect(method(HttpMethod.GET))
            .andRespond(withStatus(HttpStatus.NOT_FOUND).body("no resource"));

        assertThatThrownBy(() -> {
            restTemplate.getForObject("https://kauth.kakao.com/oauth/token?code=" + testCode,
                String.class);
        }).isInstanceOf(KakaoNotFoundException.class)
            .hasMessage("리소스를 찾을 수 없습니다. - Response body: no resource");
    }

    @Test
    @DisplayName("400 Bad Request 에러 테스트")
    void HttpClientErrorExceptionTest() {
        mockServer.expect(once(), requestTo("https://kauth.kakao.com/oauth/token?code=" + testCode))
            .andExpect(method(HttpMethod.GET))
            .andRespond(withStatus(HttpStatus.BAD_REQUEST));

        assertThatThrownBy(() -> {
            restTemplate.getForObject("https://kauth.kakao.com/oauth/token?code=" + testCode,
                String.class);
        }).isInstanceOf(HttpClientErrorException.class)
            .hasMessage("400 Bad Request");
    }

    @Test
    @DisplayName("500 Internal Server 에러 테스트")
    void HttpServerErrorExceptionTest() {
        mockServer.expect(once(), requestTo("https://kauth.kakao.com/oauth/token?code=" + testCode))
            .andExpect(method(HttpMethod.GET))
            .andRespond(withStatus(HttpStatus.INTERNAL_SERVER_ERROR));

        assertThatThrownBy(() -> {
            restTemplate.getForObject("https://kauth.kakao.com/oauth/token?code=" + testCode,
                String.class);
        }).isInstanceOf(HttpServerErrorException.class)
            .hasMessage("500 Internal Server Error");
    }
}
