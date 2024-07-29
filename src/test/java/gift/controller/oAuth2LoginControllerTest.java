package gift.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.*;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;

import com.fasterxml.jackson.databind.ObjectMapper;
import gift.response.oauth2.OAuth2TokenResponse;
import gift.service.KakaoLoginService;
import java.io.IOException;
import java.net.URI;
import java.util.Map;
import java.util.Objects;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClient.RequestHeadersSpec;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import org.springframework.web.util.DefaultUriBuilderFactory;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Mono;

/**
 * 카카오 로그인 API라는 외부 API를 사용하고 있으므로, 외부로 넘어가서 아이디와 비밀번호를 입력하는 로그인 행위 등을 수행하는 것을 테스트에서 똑같이 수행할 수가 없다.
 * 따라서 우리가 구현한 로그인 API를 테스트하기 위해서는, API를 통해 request된 작업을 처리 및 응답하는 외부(카카오) 서버 자체를 Mocking 해야한다. 1)
 * 인가 코드 요청 시(GET) 인가 코드 redirect uri의 쿼리 파라미터에 응답(302 FOUND) 2) 토큰 요청 시(POST) 메시지 바디에 액세스 토큰을 넣어
 * 응답(200 OK)
 */

@SpringBootTest
@AutoConfigureMockMvc
@ConfigurationPropertiesScan
class OAuth2LoginControllerTest {

    @Autowired
    private WebClient client;
    @Autowired
    private ObjectMapper objectMapper;
    @MockBean
    private KakaoLoginService kakaoLoginService;
    private MockWebServer mockServer;
    private String mockWebServerUri;

    @Value("${kakao.client-id}")
    private String clientId;
    @Value("${kakao.redirect-uri}")
    private String redirectUri;

    private String demoCode = "demoCode";
    private String demoToken = "demoToken";
    LinkedMultiValueMap<String, String> demoBody;

    private OAuth2TokenResponse demoTokenResponse;


    @Test
    void testProperties() {
        System.out.println("clientId = " + clientId);
        System.out.println("redirectUri = " + redirectUri);
    }

    @BeforeEach
    void setUp() throws IOException {
        mockServer = new MockWebServer();
        mockServer.start();

        demoBody = new LinkedMultiValueMap<>();
        demoBody.add("grant_type", "authorization_code");
        demoBody.add("client_id", clientId);
        demoBody.add("redirect_uri", redirectUri);
        demoBody.add("code", demoCode);
    }

    @AfterEach
    void shutDown() throws IOException {
        mockServer.shutdown();
    }

    @DisplayName("인가 코드 받아오기 테스트")
    @Test
    void authCode() throws Exception {
        //given
        String baseUri = "/oauth/authorize";
        DefaultUriBuilderFactory uriBuilderFactory = new DefaultUriBuilderFactory(baseUri);
        URI authCodeRequestUri = uriBuilderFactory.builder()
            .queryParam("scope", "talk_message")
            .queryParam("response_type", "code")
            .queryParam("redirect_uri", redirectUri)
            .queryParam("client_id", clientId)
            .build();
        mockWebServerUri = mockServer.url(authCodeRequestUri.toString()).toString();
        mockServer.enqueue(new MockResponse()
            .setResponseCode(HttpStatus.FOUND.value())
            .addHeader("Location", redirectUri + "?code=" + demoCode)
        );
        //when
        ResponseEntity<String> response = client.get()
            .uri(mockWebServerUri)
            .retrieve()
            .toEntity(String.class).block();
        //then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.FOUND);
        Map<String, String> queryParams = UriComponentsBuilder.fromUri(
                Objects.requireNonNull(response.getHeaders().getLocation()))
            .build()
            .getQueryParams()
            .toSingleValueMap();
        assertThat(queryParams.get("code")).isEqualTo(demoCode);
    }

    @DisplayName("인가 코드 받아오기 실패 테스트")
    @Test
    void failAuthCode() throws Exception {
        //given
        String baseUri = "/oauth/authorize";
        String wrongClientId = "wrong clinetId";
        String errorMessage = "access_denied";
        String errorDescriptionMessage = "User%20denied%20access";

        DefaultUriBuilderFactory uriBuilderFactory = new DefaultUriBuilderFactory(baseUri);
        URI authCodeRequestUri = uriBuilderFactory.builder()
            .queryParam("scope", "talk_message")
            .queryParam("response_type", "code")
            .queryParam("redirect_uri", redirectUri)
            .queryParam("client_id", wrongClientId)
            .build();
        mockWebServerUri = mockServer.url(authCodeRequestUri.toString()).toString();
        mockServer.enqueue(new MockResponse()
            .setResponseCode(HttpStatus.FOUND.value())
            .addHeader("Location", redirectUri + "?error=" +
                errorMessage + "&error_description=" + errorDescriptionMessage)
        );
        //when
        ResponseEntity<String> response = client.get()
            .uri(mockWebServerUri)
            .retrieve()
            .toEntity(String.class).block();
        //then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.FOUND);
        Map<String, String> queryParams = UriComponentsBuilder.fromUri(
                Objects.requireNonNull(response.getHeaders().getLocation()))
            .build()
            .getQueryParams()
            .toSingleValueMap();

        assertThat(queryParams.get("code")).isNull();
        assertThat(queryParams.get("error")).isEqualTo(errorMessage);
        assertThat(queryParams.get("error_description")).isEqualTo(errorDescriptionMessage);
    }

    @DisplayName("토큰 받아오기 테스트")
    @Test
    void token() throws Exception {
        //given
        demoTokenResponse = new OAuth2TokenResponse(demoToken, "bearer", null,
            21599, null, "talk_message");
        String tokenRequestUri = "/oauth/token";
        mockWebServerUri = mockServer.url(tokenRequestUri).toString();
        mockServer.enqueue(new MockResponse()
            .setResponseCode(HttpStatus.OK.value())
            .setBody(objectMapper.writeValueAsString(demoTokenResponse))
        );
        given(kakaoLoginService.createTokenRequest(any(String.class), any(String.class), any(String.class)))
            .willReturn(demoBody);
        //when
        LinkedMultiValueMap<String, String> body = kakaoLoginService
            .createTokenRequest(clientId, redirectUri, demoCode);
        ResponseEntity<String> response = client.post()
            .uri(mockWebServerUri)
            .contentType(MediaType.APPLICATION_FORM_URLENCODED)
            .body(Mono.just(body), LinkedMultiValueMap.class)
            .retrieve()
            .toEntity(String.class)
            .block();
        //then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        OAuth2TokenResponse tokenResponse = objectMapper.readValue(response.getBody(),
            OAuth2TokenResponse.class);
        System.out.println(response.getBody());
        assertThat(tokenResponse.accessToken()).isEqualTo(demoTokenResponse.accessToken());
    }


    @DisplayName("토큰 받아오기 싪패 테스트")
    @Test
    void failToken() throws Exception {
        //given
        demoTokenResponse = new OAuth2TokenResponse(demoToken, "bearer", null,
            21599, null, "talk_message");
        String tokenRequestUri = "/oauth/token";
        mockWebServerUri = mockServer.url(tokenRequestUri).toString();
        mockServer.enqueue(new MockResponse()
            .setResponseCode(HttpStatus.BAD_REQUEST.value())
            .setBody("error: invalid_grant")
            .setBody("error_description: authorization code not found for code=" + demoCode)
        );
        given(kakaoLoginService.createTokenRequest(any(String.class), any(String.class), any(String.class)))
            .willReturn(demoBody);
        //when //then
        LinkedMultiValueMap<String, String> body = kakaoLoginService
            .createTokenRequest(clientId, redirectUri, demoCode);
        RequestHeadersSpec<?> requestSpec = client.post()
            .uri(mockWebServerUri)
            .contentType(MediaType.APPLICATION_FORM_URLENCODED)
            .body(Mono.just(body), LinkedMultiValueMap.class);

        assertThatThrownBy(() -> client.post()
            .uri(mockWebServerUri)
            .contentType(MediaType.APPLICATION_FORM_URLENCODED)
            .body(Mono.just(body), LinkedMultiValueMap.class)
            .retrieve()
            .toEntity(String.class)
            .block())
            .isInstanceOf(WebClientResponseException.class);
    }



}