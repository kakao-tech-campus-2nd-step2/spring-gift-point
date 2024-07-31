package gift.service;

import gift.dto.KakaoTokenInfo;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.client.RestClientTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.io.IOException;

@ExtendWith(SpringExtension.class)
@RestClientTest(KaKaoService.class)
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
    void sendMessageTest() {
        // given
        String message = "test_message";
        String token = "test_token";
        String sendMessageResponse = "{\"result_code\": 0}";

        mockWebServer.enqueue(new MockResponse()
                .setResponseCode(HttpStatus.OK.value())
                .setBody(sendMessageResponse)
                .setHeader("Content-Type", "application/json;charset=UTF-8"));

        // when
        ResponseEntity<String> response = kaKaoService.sendMessage(message, token);

        // then
        System.out.println(response.getBody());
        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    void getKakaoTokenInfoTest() {
        // given
        String code = "test_code";
        String getKakaoTokenInfoResponse = "{\"token_type\":\"bearer\",\"access_token\":\"test_access_token\",\"expires_in\":43199,\"refresh_token\":\"test_refresh_token\",\"refresh_token_expires_in\":5184000}";

        mockWebServer.enqueue(new MockResponse()
                .setResponseCode(HttpStatus.OK.value())
                .setHeader("Content-Type", "application/json;charset=UTF-8")
                .setBody(getKakaoTokenInfoResponse));

        // when
        KakaoTokenInfo kakaoTokenInfo = kaKaoService.getKakaoTokenInfo(code);

        // then
        Assertions.assertThat(kakaoTokenInfo.token_type()).isEqualTo("bearer");
        Assertions.assertThat(kakaoTokenInfo.access_token()).isEqualTo("test_access_token");
        Assertions.assertThat(kakaoTokenInfo.expires_in()).isEqualTo(43199);
        Assertions.assertThat(kakaoTokenInfo.refresh_token()).isEqualTo("test_refresh_token");
        Assertions.assertThat(kakaoTokenInfo.refresh_token_expires_in()).isEqualTo(5184000);
    }

    @Test
    void getKakaoAccountEmailTest() {
        // given
        String accessToken = "test_accessToken";
        String getKakaoAccountEmailResponse = "{\"id\":3636172101,\"connected_at\":\"2024-07-24T14:39:41Z\",\"kakao_account\":{\"has_email\":true,\"email_needs_agreement\":false,\"is_email_valid\":true,\"is_email_verified\":true,\"email\":\"trichat26@naver.com\"}}";

        mockWebServer.enqueue(new MockResponse()
                .setResponseCode(HttpStatus.OK.value())
                .setHeader("Content-Type", "application/json;charset=UTF-8")
                .setBody(getKakaoAccountEmailResponse));

        // when
        String actualEmail = kaKaoService.getKakaoAccountEmail(accessToken);

        // then
        Assertions.assertThat("trichat26@naver.com").isEqualTo(actualEmail);
    }

}