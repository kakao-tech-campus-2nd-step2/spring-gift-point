package gift.api;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

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

    public OrderRequest() {}

    public OrderRequest(Long orderId, Long optionId, int quantity, String message) {
        this.orderId = orderId;
        this.optionId = optionId;
        this.quantity = quantity;
        this.message = message;
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
}