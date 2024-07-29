package gift.service.kakao;

import gift.config.RestTemplateConfig;
import gift.domain.member.SocialAccount;
import gift.domain.member.SocialType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.client.RestClientTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpMethod;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestTemplate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.*;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

@Import(RestTemplateConfig.class)
@RestClientTest(value = Oauth2TokenService.class)
public class Oauth2TokenServiceTest {

    @Autowired
    private Oauth2TokenService oauth2TokenService;

    @MockBean
    private KakaoProperties kakaoProperties;

    @Autowired
    private RestTemplate restTemplate;

    private MockRestServiceServer mockServer;

    @BeforeEach
    public void setup() {
        mockServer = MockRestServiceServer.createServer(restTemplate);
    }

    @DisplayName("Access Token 을 refresh 토큰을 통해 갱신한다.")
    @Test
    void refreshAccessToken() throws Exception {
        //given
        SocialAccount socialAccount = new SocialAccount(SocialType.KAKAO, "test-refresh-token");

        String expectedResponse = "{ \"access_token\": \"new-access-token\", \"refresh_token\": \"new-refresh-token\" }";

        mockServer.expect(requestTo("https://kauth.kakao.com/oauth/token"))
                .andExpect(method(HttpMethod.POST))
                .andExpect(content().contentType("application/x-www-form-urlencoded;charset=UTF-8"))
                .andExpect(content().string("grant_type=refresh_token&client_id=test-client-id&refresh_token=test-refresh-token"))
                .andRespond(withSuccess(expectedResponse, APPLICATION_JSON));

        given(kakaoProperties.clientId()).willReturn("test-client-id");

        //when
        oauth2TokenService.refreshAccessToken(socialAccount);

        //then
        assertThat(socialAccount.getAccessToken()).isEqualTo("new-access-token");
        assertThat(socialAccount.getRefreshToken()).isEqualTo("new-refresh-token");
        mockServer.verify();
    }

    @DisplayName("Access Token 의 만료 시간이 다 되었으면 만료된 것으로 판단한다.")
    @Test
    void isAccessTokenExpired() throws Exception {
        //given
        String accessToken = "test-access-token";
        String expectedResponse = "{ \"expires_in\": 0 }";

        mockServer.expect(requestTo("https://kapi.kakao.com/v1/user/access_token_info"))
                .andExpect(method(HttpMethod.GET))
                .andExpect(header("Authorization", "Bearer test-access-token"))
                .andRespond(withSuccess(expectedResponse, APPLICATION_JSON));

        //when
        boolean isExpired = oauth2TokenService.isAccessTokenExpired(accessToken);

        //then
        assertThat(isExpired).isTrue();
        mockServer.verify();
    }

}
