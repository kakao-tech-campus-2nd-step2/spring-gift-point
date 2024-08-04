package gift.domain.MemberDomain;

import jakarta.persistence.Embeddable;

@Embeddable
public class MemberPoint {
    private int point;

    public MemberPoint(int point) {
        this.point = point;
    }

    public MemberPoint() {

    }

    public int getPoint() {
        return point;
    }
}
