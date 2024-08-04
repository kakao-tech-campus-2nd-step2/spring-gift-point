package gift.controller.dto.response;

public record PointResponse(int point) {
    public static PointResponse of(int point) {
        return new PointResponse(point);
    }
}
