package gift.filter;

import gift.exception.BusinessException;
import gift.exception.ErrorCode;
import gift.service.TokenService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class TokenInterceptor implements HandlerInterceptor {

    @Autowired
    private TokenService tokenService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String authorizationHeader = request.getHeader(HttpHeaders.AUTHORIZATION);

        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            response.sendError(HttpStatus.UNAUTHORIZED.value(), ErrorCode.UNAUTHORIZED_REQUEST.getMessage());
            return false;
        }

        String token = authorizationHeader.substring(7);

        try {
            tokenService.validateToken(token);
        } catch (BusinessException e) {
            response.sendError(e.getErrorCode().getStatus().value(), e.getMessage());
            return false;
        }

        return true;
    }
}
