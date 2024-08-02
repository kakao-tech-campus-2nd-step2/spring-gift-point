package gift.order.domain;

import com.fasterxml.jackson.annotation.JsonValue;
import gift.global.response.ErrorCode;
import gift.order.exception.OptionValidException;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

import java.util.Objects;

@Embeddable
public class OrderQuantity {
    public static final long MIN_COUNT = 1L;
    public static final long MAX_COUNT = 100_000_000L;

    @Column(name = "quantity")
    private Long orderCountValue;

    public OrderQuantity() {
    }

    public OrderQuantity(Long orderCountValue) {
        if (orderCountValue == null || orderCountValue < MIN_COUNT || orderCountValue >= MAX_COUNT) {
            throw new OptionValidException(ErrorCode.OPTION_COUNT_OUTBOUND_ERROR);
        }
        this.orderCountValue = orderCountValue;
    }

    @JsonValue
    public Long getOrderCountValue() {
        return orderCountValue;
    }

    @Override
    public String toString() {
        return Long.toString(orderCountValue);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OrderQuantity that = (OrderQuantity) o;
        return Objects.equals(orderCountValue, that.orderCountValue);
    }

    @Override
    public int hashCode() {
        return Objects.hash(orderCountValue);
    }
}
