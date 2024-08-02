package gift.dto;

public class PointDTO {
    private Long memberId;
    private Long points;

    public PointDTO(Long memberId, Long points) {
        this.memberId = memberId;
        this.points = points;
    }

    public Long getMemberId() {
        return memberId;
    }

    public Long getPoints() {
        return points;
    }

    @Override
    public String toString() {
        return "PointDTO{" +
                "memberId=" + memberId +
                ", points=" + points +
                '}';
    }
}
