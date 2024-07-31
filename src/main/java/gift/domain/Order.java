package gift.domain;

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
    private Long productId;
    private Long optionId;
    private int quantity;
    private LocalDateTime createdAt;
    private String message;

    protected Order() {
    }

    public Order(Long productId, Long optionId, int quantity, String message) {
        this.productId = productId;
        this.optionId = optionId;
        this.quantity = quantity;
        this.message = message;
        this.createdAt = LocalDateTime.now();
    }

    public Order(Long id, Long productId, Long optionId, int quantity, String message) {
        this.id = id;
        this.productId = productId;
        this.optionId = optionId;
        this.quantity = quantity;
        this.createdAt = LocalDateTime.now();
        this.message = message;
    }

    public Long getId() {
        return id;
    }

    public Long getProductId() {
        return productId;
    }

    public Long getOptionId() {
        return optionId;
    }

    public int getQuantity() {
        return quantity;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public String getMessage() {
        return message;
    }
}
