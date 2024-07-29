package gift.auth;

import gift.model.Role;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

@Component
public class AuthApiInterceptor implements HandlerInterceptor {

    private final JwtTokenProvider tokenProvider;

    public AuthApiInterceptor(JwtTokenProvider tokenProvider) {
        this.tokenProvider = tokenProvider;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response,
        Object handler) throws Exception {

        if (handler instanceof HandlerMethod) {
            HandlerMethod handlerMethod = (HandlerMethod) handler;
            CheckRole checkRole = handlerMethod.getMethodAnnotation(CheckRole.class);

            String token = tokenProvider.extractJwtTokenFromHeader(request);

            //토큰이 존재하지 않음
            if (token == null) {
                response.setStatus(HttpStatus.UNAUTHORIZED.value());
                return false;
            }

            // 부적절한 토큰, 기간 만료 등으로 파싱 실패
            Claims claims = tokenProvider.parseToken(token);
            if (claims == null) {
                response.setStatus(HttpStatus.UNAUTHORIZED.value());
                return false;
            }

            // 접근 권한 검증
            if (checkRole != null) {
                String requiredRole = checkRole.value();
                if (!checkingRole(Role.valueOf(claims.get("member_role").toString()),
                    Role.valueOf(requiredRole))) {
                    response.setStatus(HttpStatus.FORBIDDEN.value());
                    return false;
                }
            }

            request.setAttribute("member_id", claims.get("member_id"));
        }

        return true;
    }


    public boolean checkingRole(Role memberRole, Role requiredRole) {
        return memberRole == Role.ROLE_ADMIN || memberRole.equals(requiredRole);
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
        ModelAndView modelAndView) throws Exception {
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response,
        Object handler, Exception ex) throws Exception {
    }
}
