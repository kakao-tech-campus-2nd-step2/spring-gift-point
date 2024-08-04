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

    public void subtract(Long subtractNumber) throws IllegalAccessException {
        if(point < 1000){
            throw new IllegalArgumentException("포인트는 1000부터 사용 가능합니다.");
        }
        if(point - subtractNumber < 0){
            throw new IllegalArgumentException("포인트보다 사용량이 더 많습니다.");
        }
        point -= subtractNumber;
    }
}
