package gift.interceptor;

import gift.exception.ErrorCode;
import gift.service.JwtProvider;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class AuthInterceptor implements HandlerInterceptor {

    private final JwtProvider jwtProvider;
    private final String authType;

    public AuthInterceptor(JwtProvider jwtProvider, @Value("${jwt.authType}") String authType) {
        this.jwtProvider = jwtProvider;
        this.authType = authType;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response,
        Object handler) throws Exception {
        String authHeader = request.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith(authType)) {
            throw new JwtException(ErrorCode.INVALID_TOKEN.getMessage());
        }
        String token = authHeader.substring(authType.length());
        if (!jwtProvider.validateToken(token)) {
            return false;
        }
        Long userId = Long.parseLong(jwtProvider.getUserIdFromToken(token));
        request.setAttribute("userId", userId);
        return true;
    }

}
