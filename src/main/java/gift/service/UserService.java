package gift.service;

import gift.dto.UserInfo;

public interface UserService {
    void saveUser(String accessToken, UserInfo userInfo);
}
