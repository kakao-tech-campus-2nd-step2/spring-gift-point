package gift.auth.interceptor;

import gift.member.service.JwtProvider;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Map;
import org.springframework.web.servlet.HandlerInterceptor;

public class AuthorizationInterceptor implements HandlerInterceptor {
    private final JwtProvider jwtProvider;

    public AuthorizationInterceptor(JwtProvider jwtProvider) {
        this.jwtProvider = jwtProvider;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        String authorization = request.getHeader("Authorization");
        if (authorization == null) {
            return true;
        }
        if (!authorization.startsWith("Bearer ")) {
            return setUnauthorized(response);
        }

        Map<String, Object> claims = jwtProvider.tokenToClaims(authorization.substring(7));
        if (claims == null) {
            return setUnauthorized(response);
        }

        claims.forEach(request::setAttribute);
        return true;
    }

    private boolean setUnauthorized(HttpServletResponse response) {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        return false;
    }
}
