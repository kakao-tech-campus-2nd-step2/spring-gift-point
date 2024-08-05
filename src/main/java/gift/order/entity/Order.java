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

        /*
         *  TODO: 주문 정보에서 결제 로직을 처리하지만, 순수하게 주문 정보를 저장하는 도메인과 결제 로직을 처리하는 도메인 분리 필요
         *  그 경우 PaymentInfo DTO가 요긴하게 사용될 것
         */
        totalPrice = paymentInfo.price() * quantity;
        this.discountedPrice = 0;
        if (paymentInfo.usePoint()) {
            discountedPrice = paymentInfo.usedPoint();
        }
        payedPrice = totalPrice - discountedPrice;
        accumulatedPoint = payedPrice / 10;
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
