package gift.api;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDateTime;

@Entity
@Table(name = "orders")
public class OrderRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_id", columnDefinition = "BIGINT COMMENT '주문 ID'")
    private Long orderId;

    @Column(name = "option_id", columnDefinition = "BIGINT COMMENT '옵션 ID'")
    private Long optionId;

    @Column(name = "quantity", columnDefinition = "INTEGER COMMENT '수량'")
    private int quantity;

    @Column(name = "message", columnDefinition = "VARCHAR(255) COMMENT '메시지'")
    private String message;

    @Column(name = "order_date_time", columnDefinition = "TIMESTAMP COMMENT '주문 일시'")
    private LocalDateTime orderDateTime;

    public OrderRequest() {}

    public OrderRequest(Long optionId, int quantity, String message, LocalDateTime orderDateTime) {
        this.optionId = optionId;
        this.quantity = quantity;
        this.message = message;
        this.orderDateTime = orderDateTime;
    }

    // Getters only
    public Long getOrderId() {
        return orderId;
    }

    public Long getOptionId() {
        return optionId;
    }

    public int getQuantity() {
        return quantity;
    }

    public String getMessage() {
        return message;
    }

    public LocalDateTime getOrderDateTime() {
        return orderDateTime;
    }
}