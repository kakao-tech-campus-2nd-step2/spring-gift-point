package gift.global.authentication.interceptor;

import gift.global.authentication.jwt.JwtValidator;
import gift.global.authentication.jwt.TokenType;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class TokenCheckInterceptor implements HandlerInterceptor {
    private final JwtValidator jwtValidator;

    public TokenCheckInterceptor(JwtValidator jwtValidator) {
        this.jwtValidator = jwtValidator;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        if(request.getMethod().equals(HttpMethod.OPTIONS.name())) {
            return true;
        }

        String rawToken = request.getHeader("Authorization");

        request.setAttribute("id", jwtValidator.validateAndParseToken(rawToken, TokenType.ACCESS));

        return true;
    }
}
