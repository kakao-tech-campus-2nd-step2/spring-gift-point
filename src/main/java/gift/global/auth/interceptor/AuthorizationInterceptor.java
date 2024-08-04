package gift.global.auth.interceptor;

import gift.global.auth.Authorization;
import gift.global.auth.jwt.JwtProvider;
import gift.model.member.Role;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.resource.ResourceHttpRequestHandler;

@Component
public class AuthorizationInterceptor implements HandlerInterceptor {

    private final JwtProvider jwtProvider;

    public AuthorizationInterceptor(JwtProvider jwtProvider) {
        this.jwtProvider = jwtProvider;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response,
        Object handler) throws Exception {
        if (handler instanceof HandlerMethod) {
            return handleHandler(request, response,
                ((HandlerMethod) handler).getMethodAnnotation(Authorization.class));
        } else if (handler instanceof ResourceHttpRequestHandler) {
            return handleHandler(request, response,
                handler.getClass().getAnnotation(Authorization.class));
        }
        return true;
    }

    private boolean handleHandler(HttpServletRequest request, HttpServletResponse response,
        Authorization authorization) throws Exception {
        if (!checkAuthorization(request, response, authorization)) {
            return false;
        }
        return true;
    }

    private boolean checkAuthorization(HttpServletRequest request, HttpServletResponse response,
        Authorization authorization) throws Exception {
        //필요 권한이 없는 요청의 경우
        if (authorization == null) {
            return true;
        }

        Role requiredRole = authorization.role();
        Role memberRole;

        //권한이 있는지 확인
        try {
            memberRole = Role.valueOf(request.getAttribute("roles").toString());
        } catch (Exception e) {
            sendForbiddenError(response, "Forbidden");
            return false;
        }

        //admin 계정인 경우
        if (memberRole == Role.ADMIN) {
            return true;
        }

        //요청한 권한이 사용자의 권한과 일치하는 경우
        if (requiredRole == memberRole) {
            return true;
        }

        sendForbiddenError(response, "Forbidden");
        return false;
    }

    private void sendForbiddenError(HttpServletResponse response, String message) throws Exception {
        response.sendError(HttpServletResponse.SC_FORBIDDEN, message);
    }
}
