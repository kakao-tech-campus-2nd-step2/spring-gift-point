package gift.domain.vo;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import java.util.Objects;

@Embeddable
public class Color {

    @Column(name = "color", nullable = false)
    private String value;

    protected Color() {
    }

    private Color(String value) {
        this.value = value;
    }

    public static Color from(String color) {
        return new Color(color);
    }

    public String getValue() {
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
        Color e = (Color) o;
        return Objects.equals(this.value, e.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }

    @Override
    public String toString() {
        return value;
    }
}
