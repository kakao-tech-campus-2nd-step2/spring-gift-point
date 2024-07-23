package gift.interceptor;

import gift.util.JwtUtil;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.thymeleaf.util.StringUtils;

@Component
public class JwtInterceptor implements HandlerInterceptor {
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        final String header = request.getHeader("Authorization");

        if (header == null || !header.startsWith("Bearer ")) {
            throw new JwtException("Authorization header가 없음.");
        }

        final String token = StringUtils.substringAfter(header, "Bearer ");
        Claims claims = JwtUtil.getClaims(token);
        if (claims == null || !JwtUtil.validateToken(token)) {
            throw new JwtException("claims Null이거나 token이 유효하지 않음.");
        }

        String sub = claims.get("sub", String.class);
        request.setAttribute("sub", sub);

        return true;
    }

}
