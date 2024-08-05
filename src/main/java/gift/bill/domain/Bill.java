package gift.bill.domain;

import gift.common.domain.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Bill extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long memberId;
    private Long productId;
    private Long optionId;

    private Integer quantity;
    private Integer productPrice;
    private Integer totalPrice;

    private Boolean usePoint;
    private Integer pointValue;
    private Integer accumulatedPoint;

    protected Bill() {
    }

    public Bill(Long id, Long memberId, Long productId, Long optionId,
                Integer quantity, Integer productPrice, Integer totalPrice,
                Boolean usePoint, Integer pointValue, Integer accumulatedPoint
    ) {
        this.id = id;
        this.memberId = memberId;
        this.productId = productId;
        this.optionId = optionId;
        this.quantity = quantity;
        this.productPrice = productPrice;
        this.totalPrice = totalPrice;
        this.usePoint = usePoint;
        this.pointValue = pointValue;
        this.accumulatedPoint = accumulatedPoint;
    }

    public static Bill of(Long memberId, Long productId, Long optionId,
                          Integer productPrice, Integer quantity,
                          Boolean usePoint, Integer pointValue) {
        var totalValue = quantity * productPrice;

        return new Bill(null, memberId, productId, optionId,
                quantity, productPrice, totalValue,
                usePoint, pointValue, 0);
    }

    public Integer getBilledPrice() {
        return this.totalPrice - this.pointValue;
    }

    public Long getId() {
        return id;
    }

    public Long getMemberId() {
        return memberId;
    }

    public Long getProductId() {
        return productId;
    }

    public Long getOptionId() {
        return optionId;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public Integer getProductPrice() {
        return productPrice;
    }

    public Integer getTotalPrice() {
        return totalPrice;
    }

    public Boolean getUsePoint() {
        return usePoint;
    }

    public Integer getPointValue() {
        return pointValue;
    }

    public Integer getAccumulatedPoint() {
        return accumulatedPoint;
    }

    public void setAccumulatedPoint(Integer accumulatedPoint) {
        this.accumulatedPoint = accumulatedPoint;
    }
}
