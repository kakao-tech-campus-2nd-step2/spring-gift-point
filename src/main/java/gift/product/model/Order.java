package gift.product.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import org.hibernate.annotations.CreationTimestamp;

@Entity
@Table(name = "orders")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private final Long id;

    @Column(nullable = false)
    private final Long optionId;

    @Column(nullable = false)
    private final Long memberId;

    @Column(nullable = false)
    private final int quantity;

    @Column(nullable = false)
    @CreationTimestamp
    private final LocalDateTime orderDateTime;

    @Column(nullable = false)
    private final String message;

    protected Order() {
        this(null, null, null, 0, null, null);
    }

    public Order(Long optionId, Long memberId, int quantity, String message) {
        this(null, optionId, memberId, quantity, null, message);
    }

    public Order(Long id, Long optionId, Long memberId, int quantity, String message) {
        this(id, optionId, memberId, quantity, null, message);
    }

    public Order(Long id,
        Long optionId,
        Long memberId,
        int quantity,
        LocalDateTime orderDateTime,
        String message) {
        this.id = id;
        this.optionId = optionId;
        this.memberId = memberId;
        this.quantity = quantity;
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

    public LocalDateTime getOrderDateTime() {
        return orderDateTime;
    }

    public String getMessage() {
        return message;
    }
}
