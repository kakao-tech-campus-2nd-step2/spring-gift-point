package gift.auth;

import gift.exception.oauth2.OAuth2TokenException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
public class KakaoAuthService implements OAuthService {

    public static final String X_GATEWAY_TOKEN = "X-GATEWAY-TOKEN";

    @Override
    public void getOAuthToken(HttpServletRequest request) {
        String token = request.getHeader(X_GATEWAY_TOKEN);
        if (token == null) {
            throw new OAuth2TokenException("X-GATEWAY-TOKEN 값이 정상적으로 입력되지 않았습니다. 헤더명 혹은 토큰 값을 다시 확인해주세요.");
        }
        request.setAttribute(X_GATEWAY_TOKEN, token);
    }
}
