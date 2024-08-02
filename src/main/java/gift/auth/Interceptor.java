package gift.auth;

import gift.util.errorException.BaseHandler;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.security.SignatureException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class Interceptor implements HandlerInterceptor {

    private final JwtToken jwtToken;
    private static final String AUTHORIZATION_HEADER = "Authorization";
    private static final String BEARER_PREFIX = "Bearer ";

    @Autowired
    public Interceptor(JwtToken jwtToken) {
        this.jwtToken = jwtToken;
    }

    public boolean preHandle(HttpServletRequest request, HttpServletResponse response,
        Object handler) throws Exception {

        String path = request.getRequestURI();
        String method = request.getMethod();

        if (checkMethod(path, method)) {
            return true;
        }

        String token = request.getHeader(AUTHORIZATION_HEADER);

        if (token != null && token.startsWith(BEARER_PREFIX)) {
            token = token.substring(7); // "Bearer " 제거
            try {
                Claims claims = jwtToken.validateToken(token);
                request.setAttribute("claims", claims); // 토큰에서 추출한 클레임을 요청에 설정
                return true;
            } catch (SignatureException e) {
                throw new BaseHandler(HttpStatus.UNAUTHORIZED, "잘못된 토큰 입니다.");
            } catch (ExpiredJwtException e) {
                throw new BaseHandler(HttpStatus.UNAUTHORIZED, "만료된 토큰 입니다.");
            }
        } else {
            throw new BaseHandler(HttpStatus.UNAUTHORIZED, "토큰이 존재하지 않습니다.");
        }
    }

    public boolean checkMethod(String path, String method) {
        if (method.equals("GET")) {
            if (path.equals("/api/category")) {
                return true;
            } else if (path.matches("/api/category")) {
                return true;
            } else if (path.matches("/api/category/.*")) {
                return true;
            } else if (path.matches("/api/products/.*")) {
                return true;
            } else if (path.matches("/api/products")) {
                return true;
            } else if (path.matches("/api/products/.*/options")) {
                return true;
            }
        }
        return false;
    }
}
