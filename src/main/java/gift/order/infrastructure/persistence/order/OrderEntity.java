package gift.order.infrastructure.persistence.order;

import gift.core.BaseAuditingEntity;
import gift.core.domain.order.Order;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Index;
import jakarta.persistence.Table;

@Entity
@Table(indexes = {@Index(name = "order_user_id_idx", columnList = "user_id")})
public class OrderEntity extends BaseAuditingEntity {
    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "product_id", nullable = false)
    private Long optionId;

    @Column(name = "used_point", nullable = false)
    private Long usedPoint;

    @Column(name = "quantity", nullable = false)
    private Integer quantity;

    @Column(name = "message", nullable = false)
    private String message;

    public static OrderEntity fromDomain(Order order) {
        return new OrderEntity(
                order.id(),
                order.userId(),
                order.optionId(),
                order.quantity(),
                order.message()
        );
    }

    protected OrderEntity() {
    }

    protected OrderEntity(
            Long id,
            Long userId,
            Long optionId,
            Integer quantity,
            String message
    ) {
        super(id);
        this.userId = userId;
        this.optionId = optionId;
        this.quantity = quantity;
        this.message = message;
    }

    public Long getUserId() {
        return userId;
    }

    public Long getOptionId() {
        return optionId;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public Order toDomain() {
        return Order.of(getId(), userId, optionId, usedPoint, quantity, message, getCreatedAt());
    }
}
