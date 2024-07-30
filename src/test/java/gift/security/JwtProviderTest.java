package gift.security;

import gift.common.enums.Role;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.*;

class JwtProviderTest {
    private final String secret = "secretkeysecretkeysecretkeysecretkeysecretkeysecretkey";
    private final JwtProvider jwtProvider = new JwtProvider(secret);
    @Test
    @DisplayName("Jwt Token 테스트[성공]")
    void JWtTokenTest() {
        // given
        Long id = 1L;
        String email = "test@gmail.com";
        Role role=  Role.USER;
        String token = jwtProvider.generateToken(id, email, role);

        // when
        Long resultId = jwtProvider.extractUserId(token);

        // then
        assertThat(resultId).isEqualTo(id);
    }
}