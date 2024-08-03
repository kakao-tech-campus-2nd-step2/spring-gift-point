package gift.entity.point;

import gift.entity.auth.UserEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import java.time.LocalDateTime;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity(name = "pointCharge")
@EntityListeners(AuditingEntityListener.class)
public class PointChargeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    //    포인트 충전 금액
    @Column
    private Integer price;
    //   충전 취소 여부(취소 안됨 0, 취소 됨 1)
    @Column
    private Integer isRevoke;
    //    포인트 충전일
    @CreatedDate
    private LocalDateTime transactionDate;
    //    결제 취소일
    @LastModifiedDate
    private LocalDateTime RevokeDate;
    //    포인트 충전 유저
    @ManyToOne(targetEntity = UserEntity.class, fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private UserEntity user;

    public PointChargeEntity() {

    }

    public PointChargeEntity(Integer price, UserEntity user) {
        this.price = price;
        this.user = user;
        this.isRevoke = 0;
    }

    public Long getId() {
        return id;
    }

    public Integer getPrice() {
        return price;
    }

    public LocalDateTime getTransactionDate() {
        return transactionDate;
    }

    public UserEntity getUser() {
        return user;
    }

    public void setIsRevoke(Integer isRevoke) {
        this.isRevoke = isRevoke;
    }
}
