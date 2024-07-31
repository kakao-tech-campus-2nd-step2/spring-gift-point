package gift.test.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatus;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import gift.entity.User;
import gift.repository.UserRepository;
import gift.service.KakaoAuthService;
import gift.service.TokenService;

public class KakaoAuthServiceTest {

    @Mock
    private RestTemplate restTemplate;

    @Mock
    private UserRepository userRepository;
    
    @Mock
    private TokenService tokenService;

    @InjectMocks
    private KakaoAuthService kakaoAuthService;

    private User user;
    private Map<String, String> tokenMap;
    private Map<String, Object> body;
    private String authorizationCode;
    private String accessToken;
    private String email;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        kakaoAuthService.clientId = "test-client-id";
        kakaoAuthService.redirectUri = "test-redirect-uri";
        kakaoAuthService.authUrl = "http://test-auth-url";
        kakaoAuthService.tokenInfoUrl = "http://test-token-info-url";

        user = new User("test@test.com", "pw");
        user.setId(1L);

        authorizationCode = "test-authorization-code";
        accessToken = "test-access-token";
        email = "test@test.com";

        tokenMap = new HashMap<>();
        tokenMap.put("access_token", accessToken);

        body = new HashMap<>();
        Map<String, Object> kakaoAccount = new HashMap<>();
        kakaoAccount.put("email", email);
        body.put("kakao_account", kakaoAccount);
    }

    @Test
    public void testGetAccessToken() {
        ResponseEntity<Map<String, String>> responseEntity = new ResponseEntity<>(tokenMap, HttpStatus.OK);
        ResponseEntity<Map<String, Object>> tokenInfoResponseEntity = new ResponseEntity<>(body, HttpStatus.OK);

        when(restTemplate.exchange(any(RequestEntity.class), eq(new ParameterizedTypeReference<Map<String, String>>() {})))
                .thenReturn(responseEntity);
        when(restTemplate.exchange(any(RequestEntity.class), eq(new ParameterizedTypeReference<Map<String, Object>>() {})))
                .thenReturn(tokenInfoResponseEntity);

        when(userRepository.save(any(User.class))).thenReturn(user);

        Map<String, String> result = kakaoAuthService.getAccessToken(authorizationCode);

        assertThat(result).isNotNull();
        assertThat(result.get("access_token")).isEqualTo(accessToken);
        verify(restTemplate).exchange(any(RequestEntity.class), eq(new ParameterizedTypeReference<Map<String, String>>() {}));
        verify(restTemplate).exchange(any(RequestEntity.class), eq(new ParameterizedTypeReference<Map<String, Object>>() {}));
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    public void testParseToken() {
        ResponseEntity<Map<String, Object>> responseEntity = new ResponseEntity<>(body, HttpStatus.OK);
        when(restTemplate.exchange(any(RequestEntity.class), eq(new ParameterizedTypeReference<Map<String, Object>>() {})))
                .thenReturn(responseEntity);
        
        String resultEmail = kakaoAuthService.parseToken(accessToken);
        
        verify(restTemplate).exchange(any(RequestEntity.class), eq(new ParameterizedTypeReference<Map<String, Object>>() {}));
        assertThat(resultEmail).isEqualTo(email);
    }
}
