package gift.order.domain;

import com.fasterxml.jackson.annotation.JsonValue;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

import java.util.Objects;

@Embeddable
public class OrderTotalPrice {

    @Column(name = "total_price")
    private Long orderTotalPriceValue;

    public OrderTotalPrice() {
    }

    public OrderTotalPrice(Long orderTotalPriceValue) {
    }

    @JsonValue
    public Long getOrderTotalPriceValue() {
        return orderTotalPriceValue;
    }

    @Override
    public String toString() {
        return Long.toString(orderTotalPriceValue);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OrderTotalPrice that = (OrderTotalPrice) o;
        return Objects.equals(orderTotalPriceValue, that.orderTotalPriceValue);
    }

    @Override
    public int hashCode() {
        return Objects.hash(orderTotalPriceValue);
    }
}
