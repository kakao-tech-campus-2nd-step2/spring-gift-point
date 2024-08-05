package gift.interceptor;

import gift.service.TokenService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class AuthInterceptor implements HandlerInterceptor {

    private final TokenService tokenService;

    public AuthInterceptor(TokenService tokenService) {
        this.tokenService = tokenService;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {

        if (isRequestMethodOption(request.getMethod())) {
            response.setStatus(HttpServletResponse.SC_OK);
            response.setHeader("Access-Control-Allow-Credentials", "true");
            return false;
        }

        if (!isValidateAuthHeader(request.getHeader("Authorization"))) {
            response.setHeader("WWW-Authenticate", "Bearer");
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return false;
        }

        return true;
    }

    private boolean isValidateAuthHeader(String authHeader) {
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return false;
        }

        String token = extractTokenInHeader(authHeader);
        return tokenService.isValidateToken(token);
    }

    private boolean isRequestMethodOption(String method) {
        return method.equals("OPTIONS");
    }

    private String extractTokenInHeader(String authHeader) {
        return authHeader.substring("Bearer ".length()).trim();
    }
}
