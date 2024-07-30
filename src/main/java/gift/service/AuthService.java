package gift.service;

import gift.dto.UserInfo;

public interface AuthService {
    String getAccessToken(String authorizationCode);
    UserInfo getUserInfo(String accessToken);
}
