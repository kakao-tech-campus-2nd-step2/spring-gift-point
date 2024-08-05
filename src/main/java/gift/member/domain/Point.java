package gift.member.domain;

import jakarta.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class Point implements Serializable {
    private Integer value;

    protected Point() {
    }

    private Point(Integer value) {
        this.value = value;
    }

    public static Point of(Integer value) {
        return new Point(value);
    }

    public Integer getValue() {
        return value;
    }

    public void use(Integer value) {
        if (this.value < value) {
            throw new IllegalArgumentException("포인트가 부족합니다.");
        }
        this.value -= value;
    }

    public Integer accumulate(final Integer price) {
        var accumulatedPoint = price / 10;
        this.value += accumulatedPoint;

        return accumulatedPoint;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Point point = (Point) o;
        return Objects.equals(value, point.value);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(value);
    }
}