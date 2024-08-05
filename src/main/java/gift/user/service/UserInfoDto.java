package gift.user.service;

import gift.core.domain.user.User;

public record UserInfoDto(
        Long id,
        String email,
        Long remainPoint
) {
    public static UserInfoDto of(User user, Long remainPoint) {
        return new UserInfoDto(user.id(), user.email(), remainPoint);
    }
}
