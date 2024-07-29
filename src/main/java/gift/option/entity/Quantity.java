package gift.option.entity;

import static gift.exception.ErrorMessage.OPTION_QUANTITY_SIZE;
import static gift.exception.ErrorMessage.OPTION_SUBTRACT_NOT_ALLOWED_NEGATIVE_NUMBER;

import jakarta.persistence.Embeddable;

@Embeddable
public class Quantity {

    private int value;

    public int getValue() {
        return value;
    }

    protected Quantity() {

    }

    public Quantity(int quantity) {
        validate(quantity);
        value = quantity;
    }

    public void update(int quantity) {
        validate(quantity);
        value = quantity;
    }

    public void subtract(int quantity) {
        if (isNegative(quantity)) {
            throw new IllegalArgumentException(OPTION_SUBTRACT_NOT_ALLOWED_NEGATIVE_NUMBER);
        }

        value -= quantity;
        validate(value);
    }

    private boolean isNegative(int number) {
        return number < 0;
    }

    private void validate(int quantity) {
        if (!isInRange(quantity, 0, 100_000_000)) {
            throw new IllegalArgumentException(OPTION_QUANTITY_SIZE);
        }
    }

    private boolean isInRange(int number, int min, int max) {
        return number > min && number < max;
    }
}
