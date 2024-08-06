package gift.model;

import jakarta.persistence.Access;
import jakarta.persistence.AccessType;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

@Embeddable
@Access(AccessType.FIELD)
public class Point {
    @Column(name = "point_value")
    private int value;

    public Point() {
    }

    public Point(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public void subtractPoint(int usedPoint) {
        if (usedPoint < 0) {
            throw new IllegalArgumentException("사용된 포인트가 음수일 수 없습니다.");
        }

        int remainingPoint = this.value - usedPoint;
        if (remainingPoint < 0) {
            throw new IllegalArgumentException("사용된 포인트가 갖고 있는 포인트보다 큰 값일 수 없습니다.");
        }

        this.value = remainingPoint;
    }

    public void chargePoint(int usingPoint) {
        if (usingPoint < 0) {
            throw new IllegalArgumentException("사용할 포인트가 음수일 수 없습니다.");
        }
        this.value += usingPoint;
    }

}
