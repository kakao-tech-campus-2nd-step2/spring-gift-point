package gift.auth;

import jakarta.servlet.http.HttpServletRequest;

public interface OAuthService {
    boolean getOAuthToken(HttpServletRequest request);
}
