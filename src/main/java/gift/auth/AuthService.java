package gift.auth;

import gift.entity.User;

public interface AuthService {
    String getLoginUrl();

    String generateToken(User user);
}
