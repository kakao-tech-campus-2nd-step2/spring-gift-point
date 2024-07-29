package gift.dto;

import gift.model.User;

public record KakaoUserDTO(User user, String accessToken) {
}
