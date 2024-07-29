package gift.ServiceTest;

import gift.service.JwtService;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class JwtServiceTest {
    private JwtService jwtService;

    @Mock
    private HttpServletRequest httpServletRequest;

    @BeforeEach
    void setUp() {
        jwtService = new JwtService();
    }

    @Test
    @DisplayName("JWT 생성 테스트")
    void testCreateJWT() {
        String id = "testId";
        String jwt = jwtService.createJWT(id);

        assertThat(jwt).isNotNull();
        assertThat(jwt).startsWith("eyJ");
    }

    @Test
    @DisplayName("JWT GET 테스트")
    void testGetJWT() {
        String token = "Bearer testToken";
        when(httpServletRequest.getHeader("Authorization")).thenReturn(token);

        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(httpServletRequest));

        String jwt = jwtService.getJWT();

        assertThat(jwt).isEqualTo("testToken");
    }

    @Test
    @DisplayName("JWT 토큰으로 멤버 아이디 가져오기")
    void testGetMemberId() {
        String id = "testId";
        String jwt = jwtService.createJWT(id);

        when(httpServletRequest.getHeader("Authorization")).thenReturn("Bearer " + jwt);
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(httpServletRequest));

        String memberId = jwtService.getMemberId();

        assertThat(memberId).isEqualTo(id);
    }

    @Test
    @DisplayName("유효하지 않은 토큰으로 MemberId 가져오기")
    void testGetMemberIdWithInvalidToken() {
        when(httpServletRequest.getHeader("Authorization")).thenReturn("Bearer invalidToken");
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(httpServletRequest));

        assertThatThrownBy(() -> jwtService.getMemberId())
                .isInstanceOf(Exception.class);
    }

    @Test
    @DisplayName("null 토큰으로 MemberId 가져오기")
    void testGetMemberIdWithNullToken() {
        when(httpServletRequest.getHeader("Authorization")).thenReturn(null);
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(httpServletRequest));

        assertThatThrownBy(() -> jwtService.getMemberId())
                .isInstanceOf(JwtException.class)
                .hasMessageContaining("토큰이 유효하지 않습니다.");
    }

    @Test
    @DisplayName("빈 토큰으로 MemberId 가져오기")
    void testGetMemberIdWithEmptyToken() {
        when(httpServletRequest.getHeader("Authorization")).thenReturn("Bearer ");
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(httpServletRequest));

        assertThatThrownBy(() -> jwtService.getMemberId())
                .isInstanceOf(JwtException.class)
                .hasMessageContaining("토큰이 유효하지 않습니다.");
    }
}
