package gift.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import java.util.Date;
import javax.crypto.spec.SecretKeySpec;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;

public class JwtUtilTest {
    @InjectMocks
    private JwtUtil jwtUtil;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    private Long userId = 1L;
    private String email = "admin@email.com";
    private String kakaoToken = "testAccessToken";
    private final String secretKey = "secret_key";

    @Test
    @DisplayName("토큰 생성")
    void testGenerateToken() {
        String token = jwtUtil.generateToken(email, userId, kakaoToken);

        Claims claims = Jwts.parserBuilder()
            .setSigningKey(new SecretKeySpec(jwtUtil.secretKey.getBytes(), SignatureAlgorithm.HS512.getJcaName()))
            .build()
            .parseClaimsJws(token)
            .getBody();

        assertEquals(email, claims.get("email", String.class));
        assertEquals(userId, claims.get("userId", Long.class));
        assertEquals(kakaoToken, claims.get("kakaoToken", String.class));

    }

    @Test
    @DisplayName("토큰에서 userId 추출")
    void testExtractUserId() {
        String token = jwtUtil.generateToken(email, userId, kakaoToken);
        Long extractedUserId = jwtUtil.extractUserId(token);
        assertEquals(userId, extractedUserId);
    }

    @Test
    @DisplayName("토큰에서 email 추출")
    void testExtractEmail() {
        String token = jwtUtil.generateToken(email, userId, kakaoToken);
        String extractedEmail = jwtUtil.extractEmail(token);
        assertEquals(email, extractedEmail);
    }

    @Test
    @DisplayName("access token 추출")
    void testExtractKakaoToken() {
        String token = jwtUtil.generateToken(email, userId, kakaoToken);
        String extractedKakaoToken = jwtUtil.extractKakaoToken(token);
        assertEquals(kakaoToken, extractedKakaoToken);
    }

    @Test
    @DisplayName("토큰 유효성 검사")
    void tsetValidateToken() {
        String token = jwtUtil.generateToken(email, userId, kakaoToken);
        boolean isValid = jwtUtil.validateToken(token, email);
        assertTrue(isValid);
    }

    @Test
    @DisplayName("토큰에서 만료여부 확인")
    void testIsTokenExpired() {
        Date pastDate = new Date(System.currentTimeMillis() - 1000);
        assertTrue(jwtUtil.isTokenExpired(pastDate));

        Date futureDate = new Date(System.currentTimeMillis() + 1000);
        assertFalse(jwtUtil.isTokenExpired(futureDate));
    }

}
