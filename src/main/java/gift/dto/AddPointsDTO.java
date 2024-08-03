package gift.dto;

public class AddPointsDTO {
    private Long memberId;
    private int points;

    public AddPointsDTO() {
    }

    public AddPointsDTO(Long memberId, int points) {
        this.memberId = memberId;
        this.points = points;
    }

    public Long getMemberId() {
        return memberId;
    }

    public void setMemberId(Long memberId) {
        this.memberId = memberId;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }
}
