package gift.common.auth;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class AuthInterceptor implements HandlerInterceptor {

    private final AuthService authService;

    public AuthInterceptor(AuthService authService) {
        this.authService = authService;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response,
        Object handler) {
        String authorizationHeader = request.getHeader("Authorization");

        if (!authService.validateAuthorization(authorizationHeader)) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return false;
        }

        return true;
    }

}
