package gift.entity;

import gift.domain.Wish;
import jakarta.persistence.*;

@Entity
@Table(name = "wishes")
public class WishEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private MemberEntity memberEntity;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    private ProductEntity productEntity;

    public WishEntity() {

    }

    public WishEntity(MemberEntity memberEntity, ProductEntity productEntity) {
        this.memberEntity = memberEntity;
        this.productEntity = productEntity;
    }

    public void updateWishEntity(MemberEntity memberEntity, ProductEntity productEntity) {
        this.memberEntity = memberEntity;
        this.productEntity = productEntity;
    }

    public Long getId() {
        return id;
    }

    public MemberEntity getMemberEntity() {
        return memberEntity;
    }

    public ProductEntity getProductEntity() {
        return productEntity;
    }

    public boolean equalByProductEntity(ProductEntity productEntity) {
        return this.productEntity.equals(productEntity);
    }

}
