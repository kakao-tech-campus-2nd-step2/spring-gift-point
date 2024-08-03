package gift.Model.Value;

import jakarta.persistence.Embeddable;

import java.util.Objects;

@Embeddable
public class Point {

    private int value;

    protected Point() {}

    public Point(int value) {
        validatePoint(value);

        this.value = value;
    }

    private void validatePoint(int value) {
        if(value < 0)
            throw new IllegalArgumentException("포인트는 0원 이하일 수는 없습니다");
    }

    public int getValue() {
        return value;
    }

    public void subtract(int value) {
        if(this.value - value < 0)
            throw new IllegalArgumentException("포인트는 0원 이하일 수는 없습니다. 빼려는 포인트를 조절해 주세요");

        this.value -= value;
    }

    public void add(int value) {
        this.value += value;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object)
            return true;

        if (!(object instanceof Point))
            return false;

        Point point = (Point) object;
        return Objects.equals(this.value, point.getValue());
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }

    @Override
    public String toString() {
        return String.valueOf(value);
    }
}
