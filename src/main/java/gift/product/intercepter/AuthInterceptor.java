package gift.product.intercepter;

import static gift.product.exception.GlobalExceptionHandler.INVALID_TOKEN;
import static gift.product.exception.GlobalExceptionHandler.NOT_EXIST_AUTHENTICATION;

import gift.product.exception.UnauthorizedException;
import gift.product.util.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class AuthInterceptor implements HandlerInterceptor {

    private final JwtUtil jwtUtil;
    public static final String BEARER_PREFIX = "Bearer ";
    public static final String AUTHORIZATION_HEADER = "Authorization";

    public AuthInterceptor(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String header = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (header == null || !header.startsWith(BEARER_PREFIX))
            throw new UnauthorizedException(NOT_EXIST_AUTHENTICATION);
        String token = header.substring(7);
        if(!jwtUtil.isValidToken(token))
            throw new UnauthorizedException(INVALID_TOKEN);
        return true;
    }
}
