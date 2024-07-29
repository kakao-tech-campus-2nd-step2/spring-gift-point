package gift.jwt;

import gift.AuthorizationHeader;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class JwtInterceptor implements HandlerInterceptor {

    private final JwtTokenUtil jwtTokenUtil;


    public JwtInterceptor(JwtTokenUtil jwtTokenUtil) {
        this.jwtTokenUtil = jwtTokenUtil;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String authHeader = request.getHeader("Authorization");

        AuthorizationHeader authorizationHeader = new AuthorizationHeader(authHeader);
        if (authorizationHeader.isValid()) {
            try {
                String email = jwtTokenUtil.getEmailFromToken(authorizationHeader.getToken());
                request.setAttribute("email", email);
                return true;
            } catch (Exception e) {
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Invalid or expired token");
                return false;
            }
        } else {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Authorization header missing or invalid");
            return false;
        }
    }
}