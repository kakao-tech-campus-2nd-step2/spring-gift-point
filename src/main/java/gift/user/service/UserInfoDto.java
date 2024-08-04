package gift.user.service;

import gift.core.domain.user.User;

public record UserInfoDto(
        String email,
        Long remainPoint
) {
    public static UserInfoDto of(User user, Long remainPoint) {
        return new UserInfoDto(user.email(), remainPoint);
    }
}
