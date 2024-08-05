package gift.user.dto.response;

import gift.user.entity.User;

public record PointResponse(
    Integer point
) {

    public static PointResponse from(User user) {
        return new PointResponse(user.getPoint());
    }
}
