package gift.dto;

public record PointRequest(int point) {
    public static PointRequest of(int point) {
        return new PointRequest(Math.max(point, 0));
    }
}
