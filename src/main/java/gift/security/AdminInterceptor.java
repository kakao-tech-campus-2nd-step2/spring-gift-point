package gift.security;

import gift.common.exception.AuthenticationException;
import gift.common.exception.AuthorizationException;
import gift.common.enums.Role;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.servlet.HandlerInterceptor;

public class AdminInterceptor implements HandlerInterceptor {
    private static final String AUTHORIZATION = "Authorization";
    private final JwtProvider jwtProvider;

    public AdminInterceptor(JwtProvider jwtProvider) {
        this.jwtProvider = jwtProvider;
    }

    public boolean preHandle(final HttpServletRequest request, final HttpServletResponse response, final Object handler) throws Exception {
        String token = null;
        if (request.getCookies() != null) {
            token = getAuthorization(request);
        }
        if(token == null) {
            throw new AuthenticationException("Invalid token");
        }
        try {
            Claims claims = jwtProvider.parseToken(token);
            request.setAttribute("SUB", Long.parseLong(claims.getSubject()));
            checkRole(request, claims.get("role").toString());
            return true;
        } catch (JwtException e) {
            throw new AuthenticationException(e.getMessage());
        } catch (AuthorizationException e) {
            throw new AuthorizationException(e.getMessage());
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }

    private String getAuthorization(HttpServletRequest request) {
        String token = null;
        for(Cookie cookie : request.getCookies()) {
            if (AUTHORIZATION.equals(cookie.getName())) {
                token = cookie.getValue();
            }
        }
        return token;
    }

    private void checkRole(HttpServletRequest request, String role) {
        if(!role.equals(Role.ADMIN.toString())) {
            throw new AuthorizationException("You do not have permissions to access this resource");
        }
        request.setAttribute("ROLE", role);
    }
}
