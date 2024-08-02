package gift.member.entity.embedded;

import jakarta.persistence.Embeddable;

@Embeddable
public class Point {

    private long value;

    public Point() {
        this.value = 0;
    }

    public long getValue() {
        return value;
    }

    public void subtract(long value) {
        if (value > this.value) {
            throw new IllegalArgumentException("Point not enough");
        }

        this.value = value;
    }

    public void charge(long value) {
        this.value += value;
    }
}
