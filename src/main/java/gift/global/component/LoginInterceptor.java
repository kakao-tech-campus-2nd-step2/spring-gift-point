package gift.global.component;

import gift.token.component.TokenComponent;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

// HandlerInterceptor를 구현하여 요청을 가로채는 인터셉터 클래스
// Spring bean으로 등록해서, Configurer에 의존성을 주입할 수 있도록 한다.
@Component
public class LoginInterceptor implements HandlerInterceptor {

    private final TokenComponent tokenComponent;

    @Autowired
    private LoginInterceptor(TokenComponent tokenComponent) {
        this.tokenComponent = tokenComponent;
    }

    // controller 호출 전에 가로채는 previous interceptor
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response,
        Object handler) {
        String accessToken = request.getHeader(HttpHeaders.AUTHORIZATION);
        tokenComponent.verifyAccessTokenExpiry(accessToken);

        return true;
    }
}
