package gift.auth;

import jakarta.servlet.http.HttpServletRequest;

public interface OAuthService {
    void getOAuthToken(HttpServletRequest request);
}
