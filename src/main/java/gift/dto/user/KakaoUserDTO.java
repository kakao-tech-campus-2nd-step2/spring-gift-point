package gift.dto.user;

import gift.model.User;

public record KakaoUserDTO(User user, String accessToken) {
}
