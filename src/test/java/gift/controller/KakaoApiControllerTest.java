package gift.controller;

import gift.controller.dto.TokenResponseDTO;
import gift.service.KakaoApiService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class KakaoApiControllerTest {

    @Mock
    private KakaoApiService kakaoApiService;

    @InjectMocks
    private KakaoApiController kakaoApiController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("카카오 로그인")
    void kakaoLogin_Success() {
        String expectedUrl = "https://kauth.kakao.com/oauth/authorize?client_id=test_id&redirect_uri=test_uri&response_type=code";
        when(kakaoApiService.createKakaoCode()).thenReturn(expectedUrl);

        String result = kakaoApiController.kakaoLogin();

        assertEquals("redirect:" + expectedUrl, result);
    }

    @Test
    @DisplayName("카카오 토큰 받아오기")
    void kakaoToken_Success() {
        String code = "test_code";
        String expectedToken = "test_access_token";

        when(kakaoApiService.createKakaoToken(code, null, null, null))
            .thenReturn(new TokenResponseDTO(expectedToken));

        ResponseEntity<TokenResponseDTO> response = kakaoApiController.kakaoToken(code, null, null, null);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(expectedToken, response.getBody().getToken());
    }

    @Test
    @DisplayName("카카오 토큰 에러")
    void kakaoToken_Error() {
        String error = "access_denied";
        String errorDescription = "User denied access";

        when(kakaoApiService.createKakaoToken(null, error, errorDescription, null))
            .thenThrow(new RuntimeException(errorDescription));

        assertThrows(RuntimeException.class, () ->
            kakaoApiController.kakaoToken(null, error, errorDescription, null));
    }
}
