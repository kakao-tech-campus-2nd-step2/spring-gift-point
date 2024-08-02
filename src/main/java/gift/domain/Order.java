package gift.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import org.springframework.data.annotation.CreatedDate;

@Entity
@Table(name = "customer_order")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long optionId;
    private Long memberId;
    private int quantity;
    @CreatedDate
    @Column(nullable = false, updatable = false)
    private LocalDateTime orderDateTime;
    private String message;

    public Order() {
    }

    public Order(Long optionId,Long memberId,int quantity,String message) {
        this.optionId = optionId;
        this.memberId = memberId;
        this.quantity = quantity;
        this.orderDateTime = LocalDateTime.now();
        this.message = message;
    }

    public Order(Long id, Long optionId, Long memberId,int quantity, LocalDateTime orderDateTime,
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
