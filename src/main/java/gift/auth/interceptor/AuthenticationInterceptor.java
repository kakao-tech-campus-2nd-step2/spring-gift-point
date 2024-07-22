package gift.auth.interceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Objects;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

public class AuthenticationInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        if ((handler instanceof HandlerMethod) == false) {
            return true;
        }
        if (requiredAuthorized((HandlerMethod) handler)) {
            return containsAuthority(request, response,
                    ((HandlerMethod) handler).getMethodAnnotation(Authorized.class));
        }
        return true;
    }

    private boolean requiredAuthorized(HandlerMethod handlerMethod) {
        return handlerMethod.hasMethodAnnotation(Authorized.class);
    }

    private boolean containsAuthority(HttpServletRequest request, HttpServletResponse response, Authorized authorized) {
        var value = authorized.value().getRole();

        if (Objects.isNull(request.getAttribute("role"))) {
            setUnauthorized(response);
            return false;
        }
        if (request.getAttribute("role").equals("ADMIN")) {
            return true;
        }
        if (request.getAttribute("role").equals(value)) {
            return true;
        }
        setUnauthorized(response);
        return false;
    }

    private void setUnauthorized(HttpServletResponse response) {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
    }
}
