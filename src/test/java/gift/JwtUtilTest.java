package gift;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;

import gift.util.JwtUtil;

@SpringBootTest

public class JwtUtilTest {
  @Value("${jwt.secret}")
  private String secretKey;
  @Test
  public void testGenerateToken() throws Exception {
    Long memberId = 1L;
    String email = "test@example.com";

    JwtUtil jwtUtil = new JwtUtil();
    jwtUtil.setSecretKey(secretKey);
    jwtUtil.init();

    String token = jwtUtil.generateToken(memberId, email);

    assertNotNull(token, "Generated token should not be null");
  }
}