package gift.product;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.springframework.web.servlet.HandlerInterceptor;

public class JwtCookieToHeaderInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response,
        Object handler) throws Exception {
        String accessToken = getAccessTokenFromCookie(request, response);

        if (accessToken == null) {
            return false;
        }

        request.setAttribute("CUSTOM_HEADER_AUTHORIZATION", "Bearer " + accessToken);
        return true;
    }

    private String getAccessTokenFromCookie(HttpServletRequest request,
        HttpServletResponse response)
        throws IOException {
        String accessToken = null;
        Cookie[] cookies = request.getCookies();

        if (cookies == null) {
            response.sendError(401, "액세스 토큰이 쿠키에 존재하지 않습니다.");
            return null;
        }

        for (Cookie cookie : cookies) {
            if ("accessToken".equals(cookie.getName())) {
                accessToken = cookie.getValue();
                break;
            }
        }

        if (accessToken == null) {
            response.sendError(401, "액세스 토큰이 쿠키에 존재하지 않습니다.");
        }

        return accessToken;
    }
}
