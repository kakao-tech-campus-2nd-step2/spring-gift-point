package gift.Interceptor;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.RequestScope;

import java.util.Arrays;

@Component
@RequestScope
public class TokenExtractor {

    private final String token;

    public TokenExtractor(HttpServletRequest request) {
        this.token = extractTokenFromCookie(request);
    }

    private String extractTokenFromCookie(HttpServletRequest request) {
        if (request.getCookies() != null) {
            return Arrays.stream(request.getCookies())
                    .filter(cookie -> "accessToken".equals(cookie.getName())) //카카오 토큰이면 accessToken인데 jwt면 token, 이렇게 이름이 다르다면?
                    .findFirst()
                    .map(Cookie::getValue)
                    .orElse(null);
        }
        return null;
    }

    public String getToken() {
        return token;
    }

}
