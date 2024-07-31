package gift.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Base64;

import static org.junit.jupiter.api.Assertions.*;

class TokenServiceTest {

    private TokenService tokenService;

    @BeforeEach
    void setUp() {
        tokenService = new TokenService();
    }

    @Test
    @DisplayName("토큰 생성 테스트")
    void testGenerateToken() {
        // Given
        String email = "test@example.com";
        String password = "password";

        // When
        String token = tokenService.generateToken(email, password);

        // Then
        String expectedToken = Base64.getEncoder().encodeToString((email + ":" + password).getBytes());
        assertEquals(expectedToken, token);
    }

    @Test
    @DisplayName("토큰에서 이메일 추출 테스트")
    void testExtractEmailFromToken() {
        // Given
        String email = "test@example.com";
        String password = "password";
        String token = Base64.getEncoder().encodeToString((email + ":" + password).getBytes());

        // When
        String extractedEmail = tokenService.extractEmailFromToken(token);

        // Then
        assertEquals(email, extractedEmail);
    }

    @Test
    @DisplayName("토큰 디코딩 테스트")
    void testDecodeToken() {
        // Given
        String email = "test@example.com";
        String password = "password";
        String token = Base64.getEncoder().encodeToString((email + ":" + password).getBytes());

        // When
        String[] credentials = tokenService.decodeToken(token);

        // Then
        assertEquals(2, credentials.length);
        assertEquals(email, credentials[0]);
        assertEquals(password, credentials[1]);
    }

    @Test
    @DisplayName("JWT 토큰 여부 확인 테스트")
    void testIsJwtToken() {
        // Given
        String jwtToken = "header.payload.signature";
        String nonJwtToken = Base64.getEncoder().encodeToString("test@example.com:password".getBytes());

        // When & Then
        assertTrue(tokenService.isJwtToken(jwtToken));
        assertFalse(tokenService.isJwtToken(nonJwtToken));
    }
}