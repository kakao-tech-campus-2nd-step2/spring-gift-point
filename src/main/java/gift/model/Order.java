package gift.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "orders")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "option_id", nullable = false)
    private long optionId;
    @Column(name = "quantity", nullable = false)
    private long quantity;
    @Column(name = "order_date")
    private String orderDate;
    @Column(name = "message", nullable = false)
    private String message;

    protected Order() {}

    public Order(long optionId, long quantity, String message) {
        this.optionId = optionId;
        this.quantity = quantity;
        this.message = message;
    }

    public void updateOrderDate(String orderDate) {
        this.orderDate = orderDate;
    }

    public Long getId() {
        return id;
    }

    public long getOptionId() {
        return optionId;
    }

    public long getQuantity() {
        return quantity;
    }

    public String getOrderDate() {
        return orderDate;
    }

    public String getMessage() {
        return message;
    }
}
