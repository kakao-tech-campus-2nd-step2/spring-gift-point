package gift.service.kakao;

import gift.config.RestTemplateConfig;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.client.RestClientTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestTemplate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.springframework.http.HttpMethod.POST;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.*;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

@Import(RestTemplateConfig.class)
@RestClientTest(value = KakaoApiService.class)
public class KakaoApiServiceTest {

    @Autowired
    private KakaoApiService kakaoApiService;

    @MockBean
    private KakaoProperties kakaoProperties;

    @Autowired
    private RestTemplate restTemplate;

    private MockRestServiceServer mockServer;

    @BeforeEach
    public void setup() {
        mockServer = MockRestServiceServer.createServer(restTemplate);
    }

    @DisplayName("카카오 나에게 메시지를 전송한다.")
    @Test
    void sendKakaoMessage() throws Exception {
        //given
        String accessToken = "test-access-token";
        String message = "Test Message";
        String expectedResponse = "{}";

        mockServer.expect(requestTo("https://kapi.kakao.com/v2/api/talk/memo/default/send"))
                .andExpect(method(POST))
                .andExpect(header("Authorization", "Bearer test-access-token"))
                .andRespond(withSuccess(expectedResponse, APPLICATION_JSON));

        //when
        kakaoApiService.sendKakaoMessage(accessToken, message);

        //then
        mockServer.verify();
    }

    @DisplayName("인가 코드로 카카오 엑세스 토큰을 발급받아 온다.")
    @Test
    void getTokenInfo() throws Exception {
        //given
        String code = "test-code";
        String expectedTokenResponse = "{ \"access_token\": \"test-access-token\", \"refresh_token\": \"test-refresh-token\" }";

        mockServer.expect(requestTo("https://kauth.kakao.com/oauth/token"))
                .andExpect(method(POST))
                .andExpect(content().contentType("application/x-www-form-urlencoded;charset=UTF-8"))
                .andExpect(content().string("grant_type=authorization_code&client_id=test-client-id&redirect_uri=test-redirect-uri&code=" + code))
                .andRespond(withSuccess(expectedTokenResponse, APPLICATION_JSON));

        given(kakaoProperties.clientId()).willReturn("test-client-id");
        given(kakaoProperties.redirectUrl()).willReturn("test-redirect-uri");

        //when
        KakaoTokenInfoResponse tokenResponse = kakaoApiService.getTokenInfo(code);

        //then
        assertThat(tokenResponse.getAccessToken()).isEqualTo("test-access-token");
        assertThat(tokenResponse.getRefreshToken()).isEqualTo("test-refresh-token");
        mockServer.verify();
    }

    @DisplayName("카카오 유저 정보를 가져온다.")
    @Test
    void getUserInfo() throws Exception {
        //given
        String accessToken = "test-access-token";
        String expectedUserResponse = "{ \"id\": 12345, \"properties\": { \"nickname\": \"test-nickname\" }}";

        mockServer.expect(requestTo("https://kapi.kakao.com/v2/user/me"))
                .andExpect(method(HttpMethod.GET))
                .andExpect(header("Authorization", "Bearer test-access-token"))
                .andRespond(withSuccess(expectedUserResponse, APPLICATION_JSON));

        //when
        ResponseEntity<String> response = kakaoApiService.getUserInfo(accessToken);

        //then
        assertThat(response.getStatusCode()).isEqualTo(OK);
        assertThat(response.getBody()).isEqualTo(expectedUserResponse);
        mockServer.verify();
    }

}
