package gift.service;

import gift.dto.KakaoTokenInfo;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import org.assertj.core.api.Assertions;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.client.RestClientTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.io.IOException;

@ExtendWith(SpringExtension.class)
@RestClientTest(KaKaoService.class)
@MockBean(JpaMetamodelMappingContext.class)
class KaKaoServiceTest {

    @Autowired
    private KaKaoService kaKaoService;
    private MockWebServer mockWebServer;

    @BeforeEach
    void setUp() throws IOException {
        mockWebServer = new MockWebServer();
        mockWebServer.start();

        kaKaoService.setSendMessageUrl(mockWebServer.url("/").toString());
        kaKaoService.setGetTokenUrl(mockWebServer.url("/").toString());
        kaKaoService.setGetUserInfoUrl(mockWebServer.url("/").toString());
    }

    @AfterEach
    void tearDown() throws Exception {
        mockWebServer.shutdown();
    }

    @Test
    void sendMessageTest() throws JSONException {
        // given
        String message = "testMessage";
        String token = "testToken";

        JSONObject sendMessageResponse = new JSONObject();
        sendMessageResponse.put("resultCode", 0);

        mockWebServer.enqueue(new MockResponse()
                .setResponseCode(HttpStatus.OK.value())
                .setBody(sendMessageResponse.toString())
                .setHeader("Content-Type", "application/json;charset=UTF-8"));

        // when
        ResponseEntity<String> response = kaKaoService.sendMessage(message, token);

        // then
        System.out.println(response.getBody());
        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    void getKakaoTokenInfoTest() throws JSONException {
        // given
        String code = "test_code";

        JSONObject getKakaoTokenInfoResponse = new JSONObject();
        getKakaoTokenInfoResponse.put("token_type", "bearer");
        getKakaoTokenInfoResponse.put("access_token", "test_access_token");
        getKakaoTokenInfoResponse.put("expires_in", 43199);
        getKakaoTokenInfoResponse.put("refresh_token", "test_refresh_token");
        getKakaoTokenInfoResponse.put("refresh_token_expires_in", 5184000);

        mockWebServer.enqueue(new MockResponse()
                .setResponseCode(HttpStatus.OK.value())
                .setHeader("Content-Type", "application/json;charset=UTF-8")
                .setBody(getKakaoTokenInfoResponse.toString()));

        // when
        KakaoTokenInfo kakaoTokenInfo = kaKaoService.getKakaoTokenInfo(code);

        // then
        Assertions.assertThat(kakaoTokenInfo.tokenType()).isEqualTo("bearer");
        Assertions.assertThat(kakaoTokenInfo.accessToken()).isEqualTo("test_access_token");
        Assertions.assertThat(kakaoTokenInfo.expiresIn()).isEqualTo(43199);
        Assertions.assertThat(kakaoTokenInfo.refreshToken()).isEqualTo("test_refresh_token");
        Assertions.assertThat(kakaoTokenInfo.refreshTokenExpiresIn()).isEqualTo(5184000);
    }

    @Test
    void getKakaoAccountEmailTest() throws JSONException {
        // given
        String accessToken = "test_accessToken";

        JSONObject kakaoAccount = new JSONObject();
        kakaoAccount.put("has_email", true);
        kakaoAccount.put("email_needs_agreement", false);
        kakaoAccount.put("is_email_valid", true);
        kakaoAccount.put("is_email_verified", true);
        kakaoAccount.put("email", "trichat26@naver.com");

        JSONObject getKakaoAccountEmailResponse = new JSONObject();
        getKakaoAccountEmailResponse.put("id", 3636172);
        getKakaoAccountEmailResponse.put("connected_at", "2024-07-24T14:39:41Z");
        getKakaoAccountEmailResponse.put("kakao_account", kakaoAccount);

        mockWebServer.enqueue(new MockResponse()
                .setResponseCode(HttpStatus.OK.value())
                .setHeader("Content-Type", "application/json;charset=UTF-8")
                .setBody(getKakaoAccountEmailResponse.toString()));

        // when
        String actualEmail = kaKaoService.getKakaoAccountEmail(accessToken);

        // then
        Assertions.assertThat(actualEmail).isEqualTo("trichat26@naver.com");
    }

}