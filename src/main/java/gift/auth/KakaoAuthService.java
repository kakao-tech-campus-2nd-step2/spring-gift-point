package gift.auth;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Service;

@Service
public class KakaoAuthService implements OAuthService {

    @Override
    public boolean getOAuthToken(HttpServletRequest request) {
        String token = request.getHeader("X-GATEWAY-TOKEN");
        if (token == null) {
            return false;
        }
        request.setAttribute("X-GATEWAY-TOKEN", token);
        return true;
    }
}
