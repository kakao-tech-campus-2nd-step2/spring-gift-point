package gift.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import java.time.LocalDateTime;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity(name = "PointPayment")
@EntityListeners(AuditingEntityListener.class)
public class PointPaymentEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    //    정가
    @Column
    private Integer regularPrice;
    //    결제액
    @Column
    private Integer paymentAmount;
    //    결제일시
    @CreatedDate
    private LocalDateTime transactionDate;
    //    결제 대상
    @OneToOne(mappedBy = "orders", fetch = FetchType.LAZY)
    private ProductOptionEntity productOption;
    //    결제하는 유저
    @ManyToOne(targetEntity = UserEntity.class, fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private UserEntity user;
    //    할인 정책
    @ManyToOne(targetEntity = DiscountPolicyEntity.class, fetch = FetchType.LAZY)
    @JoinColumn(name = "DiscountPolicy_id")
    private DiscountPolicyEntity discountPolicy;

    public PointPaymentEntity() {

    }

    public PointPaymentEntity(Integer regularPrice, Integer paymentAmount,
        ProductOptionEntity productOption, UserEntity user) {
        this.regularPrice = regularPrice;
        this.paymentAmount = paymentAmount;
        this.productOption = productOption;
        this.user = user;
    }

    public Long getId() {
        return id;
    }

    public Integer getRegularPrice() {
        return regularPrice;
    }

    public Integer getPaymentAmount() {
        return paymentAmount;
    }

    public LocalDateTime getTransactionDate() {
        return transactionDate;
    }

    public ProductOptionEntity getProductOption() {
        return productOption;
    }

    public UserEntity getUser() {
        return user;
    }
}
