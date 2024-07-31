package gift.domain;

import gift.domain.base.BaseEntity;
import gift.domain.base.BaseTimeEntity;
import gift.domain.constants.OrderStatus;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicInsert;

@Entity
@DynamicInsert
@Table(name = "orders")
public class Order extends BaseEntity {

    @Column(nullable = false)
    private Long memberId;

    @Column(nullable = false)
    private Long productId;

    @Column(nullable = false)
    private Long productOptionId;

    @Column(nullable = false)
    private Integer quantity;

    @Column(nullable = false)
    @ColumnDefault("''")
    private String message;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    @ColumnDefault("'ORDERED'")
    private OrderStatus orderStatus;

    protected Order() {}

    public static class Builder extends BaseTimeEntity.Builder<Builder> {

        private Long memberId;

        private Long productId;

        private Long productOptionId;

        private Integer quantity;

        private String message;

        public Builder memberId(Long memberId) {
            this.memberId = memberId;
            return this;
        }

        public Builder productId(Long productId) {
            this.productId = productId;
            return this;
        }

        public Builder productOptionId(Long productOptionId) {
            this.productOptionId = productOptionId;
            return this;
        }

        public Builder quantity(Integer quantity) {
            this.quantity = quantity;
            return this;
        }

        public Builder message(String message) {
            this.message = message;
            return this;
        }

        @Override
        protected Builder self() {
            return this;
        }

        @Override
        public Order build() {
            return new Order(this);
        }
    }

    public Order(Builder builder) {
        super(builder);
        this.memberId = builder.memberId;
        this.productId = builder.productId;
        this.productOptionId = builder.productOptionId;
        this.quantity = builder.quantity;
        this.message = builder.message;
    }

    public Long getMemberId() {
        return memberId;
    }

    public Long getProductId() {
        return productId;
    }

    public Long getProductOptionId() {
        return productOptionId;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public String getMessage() {
        return message;
    }

    public OrderStatus getOrderStatus() {
        return orderStatus;
    }

    public Order complete() {
        this.orderStatus = OrderStatus.COMPLETED;
        return this;
    }

    public Order cancel() {
        this.orderStatus = OrderStatus.CANCELED;
        return this;
    }
}
