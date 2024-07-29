package gift.order.domain;

import gift.product.option.domain.Option;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name="orders")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    @NotNull
    @Min(value = 1, message = "order: quantity는 최소 1개 이상이어야한다.")
    Long quantity;
    @NotNull
    String orderDateTime;
    @NotNull
    String message;
    @ManyToOne
    Option option;

    public Order(Long quantity, String orderDateTime, String message, Option option) {
        this.quantity = quantity;
        this.orderDateTime = orderDateTime;
        this.message = message;
        this.option = option;
    }

    public Order(Long id, Long quantity, String orderDateTime, String message, Option option) {
        this.id = id;
        this.quantity = quantity;
        this.orderDateTime = orderDateTime;
        this.message = message;
        this.option = option;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getQuantity() {
        return quantity;
    }

    public void setQuantity(Long quantity) {
        this.quantity = quantity;
    }

    public String getOrderDateTime() {
        return orderDateTime;
    }

    public void setOrderDateTime(String orderDateTime) {
        this.orderDateTime = orderDateTime;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Option getOption() {
        return option;
    }

    public void setOption(Option option) {
        this.option = option;
    }
}
