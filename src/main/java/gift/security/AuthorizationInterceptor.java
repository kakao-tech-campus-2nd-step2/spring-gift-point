package gift.security;

import gift.common.exception.AuthorizationException;
import gift.common.enums.Role;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.servlet.HandlerInterceptor;

public class AuthorizationInterceptor implements HandlerInterceptor {

    public boolean preHandle(final HttpServletRequest request, final HttpServletResponse response, final Object handler) {
        String role = (String) request.getAttribute("ROLE");
        if(role == null || !role.equals(Role.ADMIN.toString())) {
            throw new AuthorizationException("You do not have permissions to access this resource");
        }
        return true;
    }
}
