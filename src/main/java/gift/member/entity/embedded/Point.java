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

    public void subtract(long price) {
        validatePriceIsNegative(price);

        price = discount(price);

        if (price > this.value) {
            throw new IllegalArgumentException("Point not enough");
        }

        this.value -= price;
    }

    // 가격이 50,000원을 넘으면 10% 할인해준다.
    private long discount(long price) {
        if (price > 50_000) {
            return (long) (price * 0.9);
        }
        return price;
    }

    public void charge(long price) {
        validatePriceIsNegative(price);

        this.value += price;
    }

    private void validatePriceIsNegative(long price) {
        if (price < 0) {
            throw new IllegalArgumentException("Price cannot be negative");
        }
    }
}
