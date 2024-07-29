package gift.Model.Value;

import jakarta.persistence.Embeddable;

import java.util.Objects;

@Embeddable
public class Price {

    private int value;

    protected Price() {
    }

    public Price(int value) {
        validatePrice(value);

        this.value = value;
    }

    private void validatePrice(int value) {
        if (value < 0)
            throw new IllegalArgumentException("가격은 0원 이상이여야 합니다");

    }

    public int getValue() {
        return value;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object)
            return true;

        if (!(object instanceof Price))
            return false;

        Price price = (Price) object;
        return Objects.equals(this.value, price.getValue());
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
