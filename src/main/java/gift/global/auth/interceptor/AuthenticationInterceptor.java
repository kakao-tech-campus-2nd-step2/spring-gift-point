package gift.global.auth.interceptor;

import gift.global.auth.jwt.JwtProvider;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class AuthenticationInterceptor implements HandlerInterceptor {

    private final JwtProvider jwtProvider;

    public AuthenticationInterceptor(JwtProvider jwtProvider) {
        this.jwtProvider = jwtProvider;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response,
        Object handler) throws Exception {
        String authHeader = request.getHeader("Authorization");

        // Authorization 헤더가 없는 경우
        if (authHeader == null) {
            return true;
        }

        // JWT 토큰이 유효하지 않은 경우
        if (!jwtProvider.validateToken(authHeader)) {
            sendUnauthorizedError(response, "유효하지 않은 토큰입니다.");
            return false;  // 인증 실패 시 요청 처리를 중단
        }

        Claims claims = jwtProvider.getClaims(authHeader);
        request.setAttribute("memberId", claims.getSubject());
        request.setAttribute("name", claims.get("name", String.class));
        request.setAttribute("roles", claims.get("roles", String.class));
        return true;  // 인증 성공 시 다음 인터셉터나 컨트롤러로 전달
    }

    private void sendUnauthorizedError(HttpServletResponse response, String message)
        throws Exception {
        response.sendError(HttpServletResponse.SC_UNAUTHORIZED, message);
    }
}
