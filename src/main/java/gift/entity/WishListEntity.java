package gift.entity;

import jakarta.persistence.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity(name = "wishlist")
@EntityListeners(value = AuditingEntityListener.class)
public class WishListEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JoinColumn(name = "product_id")
    @ManyToOne(targetEntity = ProductEntity.class, fetch = FetchType.LAZY)
    private ProductEntity productEntity;

    @JoinColumn(name = "user_id")
    @ManyToOne(targetEntity = MemberEntity.class, fetch = FetchType.LAZY)
    private MemberEntity memberEntity;

    @CreatedDate
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime updatedAt;

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ProductEntity getProductEntity() {
        return productEntity;
    }

    public void setProductEntity(ProductEntity productEntity) {
        this.productEntity = productEntity;
    }

    public MemberEntity getMemberEntity() {
        return memberEntity;
    }

    public void setMemberEntity(MemberEntity memberEntity) {
        this.memberEntity = memberEntity;
    }
}
