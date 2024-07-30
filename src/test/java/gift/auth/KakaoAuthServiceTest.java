package gift.auth;

import gift.client.KakaoApiClient;
import gift.config.KakaoProperties;
import gift.dto.KakaoUserResponse;
import gift.entity.KakaoUser;
import gift.entity.User;
import gift.repository.KakaoUserRepository;
import gift.repository.UserRepository;
import gift.service.TokenService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;

@SpringBootTest
public class KakaoAuthServiceTest {

    @Autowired
    private KakaoAuthService kakaoAuthService;

    @MockBean
    private KakaoApiClient kakaoApiClient;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private KakaoUserRepository kakaoUserRepository;

    @MockBean
    private TokenService tokenService;

    @Autowired
    private KakaoProperties kakaoProperties;

    @AfterEach
    public void tearDown() {
        kakaoUserRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    public void 카카오_로그인_URL_생성_성공() {
        String loginUrl = kakaoAuthService.getLoginUrl();
        assertNotNull(loginUrl);
        assertTrue(loginUrl.contains("client_id=" + kakaoProperties.getClientId()));
    }

    @Test
    public void 카카오_콜백_처리_성공() {
        KakaoUserResponse kakaoUserResponse = new KakaoUserResponse(12345L, new KakaoUserResponse.Properties("testNickname"));

        Mockito.when(kakaoApiClient.getAccessToken(anyString())).thenReturn("mockAccessToken");
        Mockito.when(kakaoApiClient.getUserInfo(anyString())).thenReturn(kakaoUserResponse);
        Mockito.when(tokenService.generateToken(anyString(), anyString())).thenReturn("mockJwtToken");

        Map<String, String> tokens = kakaoAuthService.handleCallback("mockAuthorizationCode");

        assertNotNull(tokens);
        assertEquals("mockAccessToken", tokens.get("kakaoAccessToken"));
        assertEquals("mockJwtToken", tokens.get("jwtToken"));

        KakaoUser kakaoUser = kakaoUserRepository.findByKakaoId(12345L).orElse(null);
        assertNotNull(kakaoUser);
        assertEquals("testNickname", kakaoUser.getNickname());
    }

    @Test
    public void 토큰_생성_성공() {
        User user = new User("test@example.com", "password");
        userRepository.save(user);

        Mockito.when(tokenService.generateToken(anyString(), anyString())).thenReturn("mockToken");

        String token = kakaoAuthService.generateToken(user);

        assertEquals("mockToken", token);
    }
}
