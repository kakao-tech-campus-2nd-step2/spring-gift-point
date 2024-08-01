package gift.jwt;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class JwtUtilTest {

    private final JwtUtil jwtUtil = new JwtUtil("abcedfghnosibgawekbgdsjxbgkeubgiawoabhajwe");

    @Test
    @DisplayName("토큰에서 email 가져오기 테스트")
    void getEmail() {
        String token = jwtUtil.createJwt("sgoh", 1000 * 60 * 5);
        String email = jwtUtil.getEmail(token);
        assertThat(email).isEqualTo("sgoh");
    }

    @Test
    @DisplayName("토큰 유효기간 테스트")
    void isExpired() {
        String token = jwtUtil.createJwt("sgoh", 1000 * 60 * 5);
        boolean isExpired = jwtUtil.isExpired(token);
        assertThat(isExpired).isEqualTo(false);
    }

    @Test
    @DisplayName("토큰 생성 테스트")
    void createJwt() {
        String token = jwtUtil.createJwt("sgoh", 1000 * 60 * 5);
        assertThat(token).isNotNull();
    }
}