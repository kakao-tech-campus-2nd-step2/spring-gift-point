package gift.dto;

public class PointChargeRequestDto {
    private Long userId;
    private int points;

    public PointChargeRequestDto(Long userId, int points) {
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
