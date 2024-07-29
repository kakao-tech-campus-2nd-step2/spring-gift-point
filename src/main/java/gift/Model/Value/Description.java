package gift.Model.Value;

import jakarta.persistence.Embeddable;

import java.util.Objects;

@Embeddable
public class Description {

    private String value;

    protected Description() {
    }

    public Description(String value) {
        validateDescription(value);

        this.value = value;
    }

    private void validateDescription(String value) {
        if (value == null)
            throw new IllegalArgumentException("description의 값은 null이 올 수 없습니다");
    }

    public String getValue() {
        return value;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object)
            return true;

        if (!(object instanceof Description))
            return false;

        Description description = (Description) object;
        return Objects.equals(this.value, description.getValue());
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
