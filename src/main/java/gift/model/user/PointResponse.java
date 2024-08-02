package gift.model.user;

public record PointResponse(
    int point
) {
    public static PointResponse from(User user) {
        return new PointResponse(user.getPoint());
    }
}
