package gift.interceptor;

import gift.CustomAnnotation.RequestRole;
import gift.exception.ErrorCode;
import gift.model.user.Role;
import gift.service.JwtProvider;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Arrays;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
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
        Object handler) {
        HandlerMethod handlerMethod = (HandlerMethod) handler;
        String token = extractToken(request);
        jwtProvider.validateToken(token);
        checkRole(handlerMethod, token);
        Long userId = Long.parseLong(jwtProvider.getUserIdFromToken(token));
        request.setAttribute("userId", userId);
        return true;
    }

    public String extractToken(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");
        if (authHeader == null && request.getCookies() != null) {
            authHeader = Arrays.stream(request.getCookies())
                .filter(o -> o.getName().equals("Authorization")).findFirst().toString();
        }
        if (authHeader == null || !authHeader.startsWith(authType)) {
            throw new JwtException(ErrorCode.INVALID_TOKEN.getMessage());
        }
        return authHeader.substring(authType.length());
    }

    public void checkRole(HandlerMethod handlerMethod, String token) {
        RequestRole requestRole = handlerMethod.getMethodAnnotation(RequestRole.class);
        if (requestRole == null) {
            return;
        }
        Role userRole = Role.valueOf(jwtProvider.getRoleFromToken(token));
        if (!userRole.equals(requestRole.value())) {
            throw new JwtException(ErrorCode.ACCESS_DENIED.getMessage());
        }
    }

}
