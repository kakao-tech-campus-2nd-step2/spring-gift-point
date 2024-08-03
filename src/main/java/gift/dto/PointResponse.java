package gift.dto;

import gift.domain.Member;

public class PointResponse {
    private final String email;
    private final int point;

    public PointResponse(Member member) {
        this.email = member.getEmail();
        this.point = member.getPoint();
    }

    public String getEmail() {
        return email;
    }
    public int getPoint() {
        return point;
    }
}
