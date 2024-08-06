package gift.user.dto;

import gift.user.entity.User;

public record UserInfo(
    Long id,
    String email,
    Integer point
) {
    public static UserInfo from(User user) {
        return new UserInfo(user.getId(), user.getEmail(), user.getPoint());
    }
}
