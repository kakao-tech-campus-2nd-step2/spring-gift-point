package gift.dto.response;

import gift.domain.Member;

public class PointResponse {
    private int point;

    public PointResponse(int point) {
        this.point = point;
    }

    public int getPoint() {
        return point;
    }

    public static PointResponse from(Member member) {
        return new PointResponse(member.getPoint());
    }
}
