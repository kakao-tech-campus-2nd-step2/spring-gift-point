package gift.order.domain;

import com.fasterxml.jackson.annotation.JsonValue;
import gift.global.response.ErrorCode;
import gift.order.exception.OptionValidException;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

import java.util.Objects;
import java.util.regex.Pattern;

@Embeddable
public class OrderMessage {
    @Column(name = "message")
    private String orderMessageValue;

    public OrderMessage() {
    }

    public OrderMessage(String orderMessageValue) {
        this.orderMessageValue = orderMessageValue;
    }

    public String getOrderMessageValue() {
        return orderMessageValue;
    }

    @Override
    @JsonValue
    public String toString() {
        return orderMessageValue;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OrderMessage that = (OrderMessage) o;
        return Objects.equals(orderMessageValue, that.orderMessageValue);
    }

    @Override
    public int hashCode() {
        return Objects.hash(orderMessageValue);
    }
}
