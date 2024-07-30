package gift.auth;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Service;

@Service
public class KakaoAuthService implements OAuthService {

    @Override
    public boolean getOAuthToken(HttpServletRequest request) {
        String token = request.getHeader("X-OAuth-Token");
        if (token == null) {
            return false;
        }
        request.setAttribute("X-OAuth-Token", token);
        return true;
    }
}
