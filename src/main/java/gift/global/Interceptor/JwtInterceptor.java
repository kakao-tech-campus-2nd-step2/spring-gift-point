package gift.global.Interceptor;

import gift.global.exception.BusinessException;
import gift.global.exception.ErrorCode;
import gift.global.jwt.JwtProvider;
import gift.global.jwt.JwtValidator;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class JwtInterceptor implements HandlerInterceptor {

    private final JwtProvider jwtProvider;
    private final JwtValidator jwtValidator;

    public JwtInterceptor(JwtProvider jwtProvider, JwtValidator jwtValidator) {
        this.jwtProvider = jwtProvider;
        this.jwtValidator = jwtValidator;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response,
        Object handler) throws Exception {

        if (request.getHeader("Authorization") == null) {
            throw new BusinessException(ErrorCode.JWT_UNAUTHORIZED, "로그인이 필요합니다.");
        }

        String rawToken = request.getHeader("Authorization");

        String token = jwtValidator.validateFormAndRemoveBearer(rawToken);
        request.setAttribute("id", jwtProvider.getId(token));
        request.setAttribute("email", jwtProvider.getEmail(token));

        return true;
    }
}
