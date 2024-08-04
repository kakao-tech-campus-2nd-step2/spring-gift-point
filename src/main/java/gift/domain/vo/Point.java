package gift.domain.vo;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import java.util.Objects;
import org.hibernate.annotations.ColumnDefault;

@Embeddable
public class Point {

    @Column(name = "points", nullable = false)
    @ColumnDefault("10000")
    private Integer value;

    protected Point() {
    }

    private Point(Integer value) {
        this.value = value;
    }

    public static Point from(Integer points) {
        return new Point(points);
    }

    public Point subtract(Integer points) {
        this.value -= points;
        return this;
    }

    public Point add(Integer points) {
        this.value += points;
        return this;
    }

    public Integer getValue() {
        return value;
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
        return Objects.hash(value);
    }
}
