package gift.config;

import gift.auth.TokenService;
import gift.exception.type.ForbiddenException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;


@Component
public class AuthInterceptor implements HandlerInterceptor {

    private final TokenService tokenService;
    private static final String AUTHENTICATION_TYPE = "Bearer ";
    private static final int BEARER_PREFIX_LENGTH = 7;

    public AuthInterceptor(TokenService tokenService) {
        this.tokenService = tokenService;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String header = request.getHeader(AUTHORIZATION);
        if (header == null || !header.startsWith(AUTHENTICATION_TYPE)) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return false;
        }

        String token = header.substring(BEARER_PREFIX_LENGTH);
        try {
            Long memberId = tokenService.extractMemberId(token);
            request.setAttribute("memberId", memberId);
        } catch (ForbiddenException e) {
            throw new ForbiddenException("권한이 없는 요청입니다.");
        }
        return true;
    }
}
