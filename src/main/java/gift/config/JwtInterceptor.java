package gift.config;

import gift.auth.exception.InvalidAccessTokenException;
import gift.common.annotation.AllowAnonymous;
import gift.utils.JwtTokenProvider;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.cors.CorsUtils;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class JwtInterceptor implements HandlerInterceptor {

    private final JwtTokenProvider jwtTokenProvider;

    public JwtInterceptor(JwtTokenProvider jwtTokenProvider) {
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {

        if (CorsUtils.isPreFlightRequest(request)) {
            return true;
        }

        if (handler instanceof HandlerMethod handlerMethod) {   // @AllowAnonymous 어노테이션을 붙이면 세션 검증을 하지 않음
            if (handlerMethod.getMethodAnnotation(AllowAnonymous.class) != null) {
                return true;
            }
        }

        if (!jwtTokenProvider.validateToken(request.getHeader(HttpHeaders.AUTHORIZATION))) {
            throw InvalidAccessTokenException.EXCEPTION;
        }

        return true;
    }
}
