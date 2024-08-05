package gift.domain.order.entity;

import gift.exception.IllegalPriceValueException;
import jakarta.persistence.Embeddable;

@Embeddable
public record Price(int value) {

    public Price {
        if (value < 0) {
            throw new IllegalPriceValueException("error.order.negative.price.value");
        }
    }

    public Price add(int amount) {
        return new Price(value + amount);
    }

    public Price add(Price price) {
        return new Price(value + price.value);
    }

    public Price multiply(double multiplier) {
        return new Price((int) Math.ceil(value * multiplier));
    }
}
