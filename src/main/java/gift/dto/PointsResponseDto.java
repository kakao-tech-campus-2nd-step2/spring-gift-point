package gift.dto;

public class PointsResponseDto {
    private String userEmail;
    private int points;

    public PointsResponseDto() {
    }

    public PointsResponseDto(String userEmail, int points) {
        this.userEmail = userEmail;
        this.points = points;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public int getPoints() {
        return points;
    }
}
