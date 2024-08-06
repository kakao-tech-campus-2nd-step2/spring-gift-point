package gift.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDateTime;

@Entity
@Table(name = "orders")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long optionId;
    private int quantity;
    private Long pointAmount;
    private LocalDateTime orderDateTime;
    private String message;

    public Order() {
    }

    public Order(Long optionId, int quantity, Long pointAmount, LocalDateTime orderDateTime,
        String message) {
        this.optionId = optionId;
        this.quantity = quantity;
        this.pointAmount = pointAmount;
        this.orderDateTime = orderDateTime;
        this.message = message;
    }

    public Long getId() {
        return id;
    }

    public Long getOptionId() {
        return optionId;
    }

    public int getQuantity() {
        return quantity;
    }

    public Long getPointAmount() {
        return pointAmount;
    }

    public LocalDateTime getOrderDateTime() {
        return orderDateTime;
    }

    public String getMessage() {
        return message;
    }

}
