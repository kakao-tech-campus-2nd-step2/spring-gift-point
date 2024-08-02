package gift.auth;

import gift.exception.auth.FailAuthenticationException;
import gift.exception.auth.FailAuthorizationException;
import gift.model.Role;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

@Component
public class AuthApiInterceptor implements HandlerInterceptor {

    private final JwtTokenProvider tokenProvider;
    private final OAuthService oAuthService;

    public AuthApiInterceptor(JwtTokenProvider tokenProvider, OAuthService oAuthService) {
        this.tokenProvider = tokenProvider;
        this.oAuthService = oAuthService;
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
                throw new FailAuthenticationException("토큰이 존재하지 않습니다.");
            }

            // 부적절한 토큰, 기간 만료 등으로 파싱 실패
            Claims claims = tokenProvider.parseToken(token);
            if (claims == null) {
                throw new FailAuthenticationException("토큰이 유효하지 않습니다.");
            }

            // 접근 권한 검증
            if (checkRole != null) {
                String requiredRole = checkRole.value();
                if (!checkingRole(Role.valueOf(claims.get("member_role").toString()),
                    Role.valueOf(requiredRole))) {
                    throw new FailAuthorizationException("접근 권한이 없습니다.");
                }
            }

            // 주문 시 카카오톡 메시지 API 사용(oAuth 액세스 토큰 필요)
            if (HttpMethod.POST.matches(request.getMethod())
                && request.getRequestURI().startsWith("/api/orders")) {
                oAuthService.getOAuthToken(request);
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
