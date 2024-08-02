package gift.dto;

public class PointResponseDto {
    private Long userId;
    private int points;

    public PointResponseDto(Long userId, int points) {
        this.userId = userId;
        this.points = points;
    }

    public Long getUserId() {
        return userId;
    }

    public int getPoints() {
        return points;
    }
}
