package gift.service;

import static org.junit.jupiter.api.Assertions.assertEquals;

import gift.dto.KakaoProperties;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.util.UriComponentsBuilder;

@SpringBootTest
public class KakaoUserServiceTest {
    @Autowired
    private KakaoProperties kakaoProperties;

    @Autowired
    private KakaoUserService kakaoUserService;

    private String expectedUrl;
    @BeforeEach
    void setUp() {
        // Given: Set up the expected URL based on KakaoProperties
        expectedUrl = UriComponentsBuilder.fromHttpUrl("https://kauth.kakao.com/oauth/authorize")
            .queryParam("scope", "talk_message")
            .queryParam("response_type", "code")
            .queryParam("redirect_uri", kakaoProperties.redirectUrl())
            .queryParam("client_id", kakaoProperties.clientId())
            .toUriString();
    }

    @Test
    @DisplayName("인가 코드 발급")
    void testGetAuthorizeUrl() {
        String authorizeCode = kakaoUserService.getAuthorizeUrl();
        assertEquals(expectedUrl, authorizeCode);
    }

}