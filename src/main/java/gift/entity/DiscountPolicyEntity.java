package gift.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import gift.entity.enums.DiscountType;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity(name = "DiscountPolicy")
@EntityListeners(AuditingEntityListener.class)
public class DiscountPolicyEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    //    할인 타입(정액, 퍼센트)
    @Column
    private DiscountType discountType;
    //    할인율 or 할인금액
    @Column
    private Integer discount;
    //    할인 종료 일시
    @Column
    private LocalDateTime endDate;
    //    할인 한도
    @Column
    private Integer DiscountAmountLimit;
    //    정책 생성일
    @CreatedDate
    private LocalDateTime createdAt;
    //    대상 상품
    @ManyToOne(targetEntity = ProductEntity.class, fetch = FetchType.LAZY)
    @JoinColumn(name = "productOption_id")
    private ProductOptionEntity target;
    //    할인이 적용된 결제건
    @JsonIgnore
    @OneToMany(mappedBy = "discountPolicy", fetch = FetchType.LAZY,
        cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PointPaymentEntity> PointPayment;

    public DiscountPolicyEntity() {
    }

    public DiscountPolicyEntity(DiscountType discountType, Integer discount,
        LocalDateTime endDate, Integer discountAmountLimit, ProductOptionEntity target,
        List<PointPaymentEntity> pointPayment) {
        this.discountType = discountType;
        this.discount = discount;
        this.endDate = endDate;
        DiscountAmountLimit = discountAmountLimit;
        this.target = target;
        PointPayment = pointPayment;
    }

    public Long getId() {
        return id;
    }

    public DiscountType getDiscountType() {
        return discountType;
    }

    public Integer getDiscount() {
        return discount;
    }

    public LocalDateTime getEndDate() {
        return endDate;
    }

    public Integer getDiscountAmountLimit() {
        return DiscountAmountLimit;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public ProductOptionEntity getTarget() {
        return target;
    }

    public List<PointPaymentEntity> getPointPayment() {
        return PointPayment;
    }
}
