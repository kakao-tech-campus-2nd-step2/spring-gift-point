package gift.dto;

public class PointRequest {
    private Long member_id;
    private Long point;

    public PointRequest() {

    }

    public Long getMember_id() {
        return member_id;
    }

    public void setMember_id(Long member_id) {
        this.member_id = member_id;
    }

    public Long getPoint() {
        return point;
    }

    public void setPoint(Long point) {
        this.point = point;
    }
}
