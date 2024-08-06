package gift.order.entity;

import gift.common.entity.BaseEntity;
import gift.order.dto.PaymentInfo;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "orders")
public class Order extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long optionId;

    @Column(nullable = false)
    private Integer quantity;

    @Column(nullable = false)
    private String message;

    @Column(nullable = false)
    private Integer totalPrice;

    @Column(nullable = false)
    private Integer payedPrice;

    @Column(nullable = false)
    private Integer discountedPrice;

    @Column(nullable = false)
    private Integer accumulatedPoint;

    public Order(PaymentInfo paymentInfo) {
        this.optionId = paymentInfo.optionId();
        this.quantity = paymentInfo.quantity();
        this.message = paymentInfo.message();
        this.totalPrice = paymentInfo.totalPrice();
        this.payedPrice = paymentInfo.payedPrice();
        this.discountedPrice = paymentInfo.discountedPrice();
        this.accumulatedPoint = paymentInfo.accumulatedPrice();
    }

    protected Order() {
    }

    public Long getId() {
        return id;
    }

    public Integer getTotalPrice() {
        return totalPrice;
    }

    public Integer getDiscountedPrice() {
        return discountedPrice;
    }

    public Integer getAccumulatedPoint() {
        return accumulatedPoint;
    }

}
