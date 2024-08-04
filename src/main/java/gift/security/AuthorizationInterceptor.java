package gift.security;

import gift.common.exception.AuthorizationException;
import gift.common.enums.Role;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.servlet.HandlerInterceptor;

public class AuthorizationInterceptor implements HandlerInterceptor {

    public boolean preHandle(final HttpServletRequest request, final HttpServletResponse response, final Object handler) {

        if ("OPTIONS".equalsIgnoreCase(request.getMethod())) {
            return true;
        }

        String role = (String) request.getAttribute("ROLE");
        if(role == null || !role.equals(Role.ADMIN.toString())) {
            throw new AuthorizationException("관리자만 접근할 수 있습니다.");
        }
        return true;
    }
}
