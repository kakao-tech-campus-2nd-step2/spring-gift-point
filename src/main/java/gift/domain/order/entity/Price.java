package gift.domain.order.entity;

import gift.exception.IllegalPriceValueException;
import jakarta.persistence.Embeddable;
import java.util.Objects;

@Embeddable
public class Price {
    private int value;

    public Price(int value) {
        if (value < 0) {
            throw new IllegalPriceValueException("error.order.negative.price.value");
        }
        this.value = value;
    }

    protected Price() {

    }

    public int getValue() {
        return value;
    }

    public Price add(int amount) {
        return new Price(value + amount);
    }

    public Price add(Price price) {
        return new Price(value + price.value);
    }

    public Price multiply(double multiplier) {
        return new Price((int) (value * multiplier));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Price price = (Price) o;
        return this.value == price.value;
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.value);
    }
}
