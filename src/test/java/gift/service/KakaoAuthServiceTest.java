package gift.service;

import gift.dto.KakaoProperties;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

public class KakaoAuthServiceTest {

    @Mock
    private RestTemplate kakaoRestTemplate;

    @Mock
    private KakaoProperties kakaoProperties;

    @InjectMocks
    private KakaoAuthService kakaoAuthService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAccessToken() {
        // Given
        String code = "test-code";
        String clientId = "test-client-id";
        String redirectUri = "http://test.com";
        String accessToken = "test-access-token";
        String url = "https://kauth.kakao.com/oauth/token";

        when(kakaoProperties.getClientId()).thenReturn(clientId);
        when(kakaoProperties.getRedirectUri()).thenReturn(redirectUri);

        Map<String, String> responseBody = new HashMap<>();
        responseBody.put("access_token", accessToken);
        ResponseEntity<Map> responseEntity = new ResponseEntity<>(responseBody, HttpStatus.OK);

        when(kakaoRestTemplate.exchange(eq(url), eq(HttpMethod.POST), any(HttpEntity.class), eq(Map.class)))
                .thenReturn(responseEntity);

        // When
        String result = kakaoAuthService.getAccessToken(code);

        // Then
        assertEquals(accessToken, result);
        verify(kakaoRestTemplate, times(1)).exchange(eq(url), eq(HttpMethod.POST), any(HttpEntity.class), eq(Map.class));
    }

    @Test
    void testGetAccessTokenFailure() {
        // Given
        String code = "test-code";
        String url = "https://kauth.kakao.com/oauth/token";

        when(kakaoRestTemplate.exchange(eq(url), eq(HttpMethod.POST), any(HttpEntity.class), eq(Map.class)))
                .thenThrow(new RuntimeException("Failed to get access token"));

        // When & Then
        RuntimeException thrown = assertThrows(RuntimeException.class, () -> kakaoAuthService.getAccessToken(code));
        assertEquals("엑세스 토큰을 받을 수 없습니다.", thrown.getMessage());
    }

    @Test
    void testValidateAccessToken() {
        // Given
        String accessToken = "test-access-token";
        String url = "https://kapi.kakao.com/v1/user/access_token_info";

        Map<String, String> responseBody = new HashMap<>();
        ResponseEntity<Map> responseEntity = new ResponseEntity<>(responseBody, HttpStatus.OK);

        when(kakaoProperties.getAccessTokenInfoUrl()).thenReturn(url);
        when(kakaoRestTemplate.exchange(eq(url), eq(HttpMethod.GET), any(HttpEntity.class), eq(Map.class)))
                .thenReturn(responseEntity);

        // When
        kakaoAuthService.validateAccessToken(accessToken);

        // Then
        verify(kakaoRestTemplate, times(1)).exchange(eq(url), eq(HttpMethod.GET), any(HttpEntity.class), eq(Map.class));
    }

    @Test
    void testValidateAccessTokenFailure() {
        // Given
        String accessToken = "test-access-token";
        String url = "https://kapi.kakao.com/v1/user/access_token_info";

        when(kakaoProperties.getAccessTokenInfoUrl()).thenReturn(url);
        when(kakaoRestTemplate.exchange(eq(url), eq(HttpMethod.GET), any(HttpEntity.class), eq(Map.class)))
                .thenThrow(new RuntimeException("Failed to validate access token"));

        // When & Then
        RuntimeException thrown = assertThrows(RuntimeException.class, () -> kakaoAuthService.validateAccessToken(accessToken));
        assertEquals("엑세스 토큰 검증에 실패했습니다.", thrown.getMessage());
    }
}