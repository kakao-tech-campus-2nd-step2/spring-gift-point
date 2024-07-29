package gift.Interceptor;

import gift.service.BasicTokenService;
import jakarta.annotation.Nullable;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

public class TokenInterceptor implements HandlerInterceptor {

    private final BasicTokenService basicTokenService;

    public TokenInterceptor(BasicTokenService basicTokenService) {
        this.basicTokenService = basicTokenService;
    }

    @Override
    public boolean preHandle(
            HttpServletRequest request,
            HttpServletResponse response,
            Object handler
    ) throws Exception {

        String token = request.getHeader("Token");

        if (token == null || token.isEmpty()) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return false;
        }

        response.setHeader("UserId", basicTokenService.getUserIdByDecodeTokenValue(token).toString());

        return true;
    }

    @Override
    public void postHandle(
            HttpServletRequest request,
            HttpServletResponse response,
            Object handler,
            @Nullable ModelAndView modelAndView
    ) throws Exception {

        String userId = request.getHeader("UserId");

        if (userId == null || userId.isEmpty()) {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            return;
        }

        String newToken = basicTokenService.makeTokenFrom(userId);
        response.setHeader("Token", newToken);

    }
}
