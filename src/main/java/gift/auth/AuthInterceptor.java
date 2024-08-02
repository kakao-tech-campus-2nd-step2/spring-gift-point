package gift.auth;

import gift.auth.exception.AuthException;
import gift.auth.utill.JwtToken;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class AuthInterceptor implements HandlerInterceptor {

    private final JwtToken jwtToken = new JwtToken();

    /**
     * Authorization 헤더를 검증합니다.
     *
     * @param request  current HTTP request
     * @param response current HTTP response
     * @param handler  chosen handler to execute, for type and/or instance evaluation
     * @return true if the execution chain should proceed with the next interceptor or the handler
     * itself.
     * @throws Exception in case of errors
     */
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response,
                             Object handler) throws Exception {
        String token = request.getHeader("Authorization");

        if (token == null || !token.startsWith("Bearer ")) {
            throw new AuthException("토큰이 존재하지 않습니다.", HttpStatus.FORBIDDEN);
        }

        token = token.substring(7);
        try {
            Claims claims = jwtToken.validateToken(token);
            request.setAttribute("claims", claims);
            return true;
        } catch (ExpiredJwtException e) {
            throw new AuthException("토큰이 만료되었습니다.", HttpStatus.UNAUTHORIZED);
        } catch (UnsupportedJwtException e) {
            throw new AuthException("지원되지 않는 토큰 형식입니다.", HttpStatus.UNAUTHORIZED);
        } catch (MalformedJwtException e) {
            throw new AuthException("잘못된 토큰입니다.", HttpStatus.UNAUTHORIZED);
        } catch (SecurityException e) {
            throw new AuthException("토큰 서명이 유효하지 않습니다.", HttpStatus.UNAUTHORIZED);
        } catch (IllegalArgumentException e) {
            throw new AuthException("토큰이 잘못되었습니다.", HttpStatus.UNAUTHORIZED);
        }
    }
}
