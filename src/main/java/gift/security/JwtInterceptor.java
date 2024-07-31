package gift.security;

import gift.common.exception.AuthenticationException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.servlet.HandlerInterceptor;

public class JwtInterceptor implements HandlerInterceptor {
    private static final String HEADER_AUTHORIZATION = "Authorization";
    private static final String TOKEN_PREFIX = "Bearer ";
    private final JwtProvider jwtProvider;

    public JwtInterceptor(JwtProvider jwtProvider) {
        this.jwtProvider = jwtProvider;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String authHeader = request.getHeader(HEADER_AUTHORIZATION);
        if(authHeader == null || !authHeader.startsWith(TOKEN_PREFIX)) {
            return true;
        }

        String token = authHeader.substring(TOKEN_PREFIX.length());
        try {
            Claims claims = jwtProvider.parseToken(token);
            request.setAttribute("SUB", Long.parseLong(claims.getSubject()));
            request.setAttribute("ROLE", claims.get("role"));
            return true;
        } catch (JwtException e) {
            throw new AuthenticationException(e.getMessage());
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }
}
