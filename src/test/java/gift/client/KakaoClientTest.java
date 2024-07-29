package gift.client;

import gift.kakao.client.KakaoClient;
import gift.kakao.auth.dto.KakaoTokenResponse;
import gift.kakao.vo.KakaoProperties;
import gift.global.config.RestTemplateConfig;
import gift.global.error.CustomException;
import gift.global.error.ErrorCode;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.client.RestClientTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestTemplate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.content;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.header;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

@Import(RestTemplateConfig.class)
@RestClientTest(value = KakaoClient.class)
class KakaoClientTest {

    @Autowired
    private KakaoClient kakaoClient;

    @MockBean
    private KakaoProperties kakaoProperties;

    @Autowired
    private RestTemplate restTemplate;

    private MockRestServiceServer server;

    @BeforeEach
    void setUp() {
        server = MockRestServiceServer.createServer(restTemplate);
    }

    @Test
    @DisplayName("액세스 토큰 발급 확인 테스트")
    void getAccessToken() {
        String code = "test-code";
        String responseBody = "{ \"access_token\": \"test-token\"," +
                "\"refresh_token\": \"test-refresh-token\"}";
        String responseToken = "test-token";
        String responseRefreshToken = "test-refresh-token";
        String url = "https://kauth.kakao.com/oauth/token";
        given(kakaoProperties.authDomainName())
                .willReturn("https://kauth.kakao.com");

        server.expect(requestTo(url))
                .andExpect(method(HttpMethod.POST))
                .andExpect(content().contentType(MediaType.APPLICATION_FORM_URLENCODED))
                .andRespond(withSuccess(responseBody, MediaType.APPLICATION_JSON));

        KakaoTokenResponse response = kakaoClient.getTokenResponse(code);

        assertThat(response.accessToken()).isEqualTo(responseToken);
        assertThat(response.refreshToken()).isEqualTo(responseRefreshToken);
        server.verify();
    }

    @Test
    @DisplayName("카카오 사용자 ID 확인 테스트")
    void getUserId() {
        String token = "test-token";
        String responseBody = "{ \"id\": \"123\"}";
        String url = "https://kapi.kakao.com/v2/user/me";
        String authPrefix = "Bearer ";
        String authHeader = authPrefix + token;
        Long userInfoId = 123L;
        given(kakaoProperties.authorizationPrefix())
                .willReturn(authPrefix);
        given(kakaoProperties.apiDomainName())
                .willReturn("https://kapi.kakao.com");

        server.expect(requestTo(url))
                .andExpect(header(HttpHeaders.AUTHORIZATION, authHeader))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withSuccess(responseBody, MediaType.APPLICATION_JSON));

        Long userInfoResponse = kakaoClient.getUserId(token);

        assertThat(userInfoResponse).isEqualTo(userInfoId);
        server.verify();
    }

    @Test
    @DisplayName("토큰 갱신 확인 테스트")
    void getRefreshTokenResponse() {
        String refreshToken = "test-refresh-token";
        String responseBody = "{ \"access_token\": \"test-token\"," +
                "\"refresh_token\": \"test-refresh-token\"}";
        String newAccessToken = "test-token";
        String newRefreshToken = "test-refresh-token";
        String url = "https://kauth.kakao.com/oauth/token";

        given(kakaoProperties.authDomainName())
                .willReturn("https://kauth.kakao.com");

        server.expect(requestTo(url))
                .andExpect(method(HttpMethod.POST))
                .andExpect(content().contentType(MediaType.APPLICATION_FORM_URLENCODED))
                .andRespond(withSuccess(responseBody, MediaType.APPLICATION_JSON));

        KakaoTokenResponse refreshTokenResponse = kakaoClient.getRefreshTokenResponse(refreshToken);

        assertThat(refreshTokenResponse.accessToken()).isEqualTo(newAccessToken);
        assertThat(refreshTokenResponse.refreshToken()).isEqualTo(newRefreshToken);
        server.verify();
    }

    @Test
    @DisplayName("카카오톡 메세지 보내기 확인 테스트")
    void sendMessageToMe() {
        String url = "https://kapi.kakao.com/v2/api/talk/memo/default/send";
        String token = "test-token";
        String responseBody = "{ \"result_code\": \"0\"}";
        String text = "message";
        String path = "/";
        given(kakaoProperties.apiDomainName())
                .willReturn("https://kapi.kakao.com");
        server.expect(requestTo(url))
                .andExpect(method(HttpMethod.POST))
                .andExpect(content().contentType(MediaType.APPLICATION_FORM_URLENCODED + ";charset=UTF-8"))
                .andRespond(withSuccess(responseBody, MediaType.APPLICATION_JSON));

        kakaoClient.sendMessageToMe(token, text, path);
        server.verify();
    }

    @Test
    @DisplayName("카카오톡 메세지 보내기 실패 테스트")
    void sendMessageToMeFailed() {
        String url = "https://kapi.kakao.com/v2/api/talk/memo/default/send";
        String token = "test-token";
        String responseBody = "{ \"result_code\": \"-1\"}";
        String text = "message";
        String path = "/";
        given(kakaoProperties.apiDomainName())
                .willReturn("https://kapi.kakao.com");
        server.expect(requestTo(url))
                .andExpect(method(HttpMethod.POST))
                .andExpect(content().contentType(MediaType.APPLICATION_FORM_URLENCODED + ";charset=UTF-8"))
                .andRespond(withSuccess(responseBody, MediaType.APPLICATION_JSON));

        assertThatThrownBy(() -> kakaoClient.sendMessageToMe(token, text, path))
                .isInstanceOf(CustomException.class)
                .hasMessage(ErrorCode.EXTERNAL_API_UNAVAILABLE
                                     .getMessage());
        server.verify();
    }

}