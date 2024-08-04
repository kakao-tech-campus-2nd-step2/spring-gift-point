package gift.service;

import static org.assertj.core.api.Assertions.assertThat;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;

@SpringBootTest
public class KakaoServiceTest {
    /*@Autowired
    private KakaoService kakaoService;

    private MockWebServer mockWebServer;

    private String mockWebServerUrl;

    private final String token = "temp_token";

    @BeforeEach
    void setUp() throws IOException {
        mockWebServer = new MockWebServer();
        mockWebServer.start();
        mockWebServerUrl = mockWebServer.url("/").toString();

    }
    @AfterEach
    void terminate() throws IOException {
        mockWebServer.shutdown();
    }

    @Test
    void getAccessTokenFromKakao_respond_successfully() {
        //given
        mockWebServer.enqueue(new MockResponse()
                .setResponseCode(200)
                .setBody("{\"access_token\":\"temp_token\",\"token_type\":\"bearer\",\"expires_in\":43199}")
                .addHeader("Content-Type", "application/json"));

        String responseToken = kakaoService.getAccessTokenFromKakao("temp_code");
        //then
        assertThat(responseToken).isEqualTo("temp_token");
    }*/
}