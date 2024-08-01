package gift;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import gift.domain.model.dto.KakaoTokenResponseDto;
import gift.domain.model.dto.TokenResponseDto;
import gift.service.KakaoLoginService;
import gift.service.UserService;
import gift.controller.KakaoLoginController;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

class KakaoLoginControllerTest {

    @Mock
    private KakaoLoginService kakaoLoginService;

    @Mock
    private UserService userService;

    private KakaoLoginController kakaoLoginController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        kakaoLoginController = new KakaoLoginController(kakaoLoginService, userService);
    }

    @Test
    @DisplayName("카카오 로그인 콜백 처리 - 인증 코드로 카카오 토큰을 받고 사용자 로그인/등록 후 JWT 토큰 반환")
    void callbackTest() {
        // Given
        String code = "test_code";
        KakaoTokenResponseDto kakaoTokens = new KakaoTokenResponseDto(
            "bearer",
            "test_access_token",
            null,
            3600,
            "test_refresh_token",
            5184000,
            "profile"
        );
        TokenResponseDto expectedTokenResponse = new TokenResponseDto("jwt_token");

        when(kakaoLoginService.getTokensFromKakao(code)).thenReturn(kakaoTokens);
        when(userService.loginOrRegisterKakaoUser(kakaoTokens)).thenReturn(expectedTokenResponse);

        // When
        ResponseEntity<TokenResponseDto> response = kakaoLoginController.callback(code);

        // Then
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(expectedTokenResponse, response.getBody());

        verify(kakaoLoginService).getTokensFromKakao(code);
        verify(userService).loginOrRegisterKakaoUser(kakaoTokens);
    }
}