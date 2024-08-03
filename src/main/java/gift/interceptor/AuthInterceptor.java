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

        if (isRequestMethodOption(request, response)) {
            return false;
        }

        String token = validateAndExtractToken(request, response);
        if (token == null) {
            return false;
        }

        request.setAttribute("memberId", tokenService.getMemberId(token));
        return true;
    }

    private String validateAndExtractToken(HttpServletRequest request, HttpServletResponse response) {
        String authHeader = request.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            response.setHeader("WWW-Authenticate", "Bearer");
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return null;
        }
        String token = authHeader.substring("Bearer ".length()).trim();
        if (!tokenService.isValidateToken(token)) {
            response.setHeader("WWW-Authenticate", "Bearer");
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return null;
        }
        return token;
    }

    private boolean isRequestMethodOption(HttpServletRequest request, HttpServletResponse response) {
        if (request.getMethod().equals("OPTIONS")) {
            response.setStatus(HttpServletResponse.SC_OK);
            response.setHeader("Access-Control-Allow-Credentials", "true");
            return true;
        }
        return false;
    }
}
