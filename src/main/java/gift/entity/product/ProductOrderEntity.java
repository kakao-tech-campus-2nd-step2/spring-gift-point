package gift.entity.product;

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
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity(name = "ProductOrder")
@EntityListeners(AuditingEntityListener.class)
public class ProductOrderEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private Long quantity;

    @CreatedDate
    private LocalDateTime createdAt;

    @ManyToOne(targetEntity = UserEntity.class, fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private UserEntity userEntity;

    @ManyToOne(targetEntity = ProductOptionEntity.class, fetch = FetchType.LAZY)
    @JoinColumn(name = "productOption_id")
    private ProductOptionEntity options;

    public ProductOrderEntity() {
    }

    public ProductOrderEntity(Long quantity, UserEntity userEntity,
        ProductOptionEntity options) {
        this.quantity = quantity;
        this.userEntity = userEntity;
        this.options = options;
    }

    public Long getId() {
        return id;
    }

    public Long getQuantity() {
        return quantity;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public UserEntity getUserEntity() {
        return userEntity;
    }

    public ProductOptionEntity getOptions() {
        return options;
    }
}
