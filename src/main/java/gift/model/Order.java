package gift.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "orders")
public class Order extends BaseEntity {
    @Column(name = "option_id", nullable = false)
    private long optionId;
    @Column(name = "quantity", nullable = false)
    private long quantity;
    @Column(name = "message", nullable = false)
    private String message;

    protected Order() {}

    public Order(long optionId, long quantity, String message) {
        this.optionId = optionId;
        this.quantity = quantity;
        this.message = message;
    }

    public long getOptionId() {
        return optionId;
    }

    public long getQuantity() {
        return quantity;
    }

    public String getMessage() {
        return message;
    }
}
