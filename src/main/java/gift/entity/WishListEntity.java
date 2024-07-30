package gift.entity;

import gift.domain.WishList;
import jakarta.persistence.*;

@Entity
@Table(name = "wish_lists")
public class WishListEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private MemberEntity memberEntity;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    private ProductEntity productEntity;

    public WishListEntity() {

    }

    public WishListEntity(MemberEntity memberEntity, ProductEntity productEntity) {
        this.memberEntity = memberEntity;
        this.productEntity = productEntity;
    }

    public WishListEntity(Long id, MemberEntity memberEntity, ProductEntity productEntity) {
        this.id = id;
        this.memberEntity = memberEntity;
        this.productEntity = productEntity;
    }

    public Long getId() {
        return id;
    }

    public MemberEntity getMemberEntity() {
        return memberEntity;
    }

    public void setMemberId(MemberEntity memberEntity) {
        this.memberEntity = memberEntity;
    }

    public ProductEntity getProductEntity() {
        return productEntity;
    }

    public void setProductId(ProductEntity productEntity) {
        this.productEntity = productEntity;
    }

    public boolean equalByProductEntity(ProductEntity productEntity) {
        return this.productEntity.equals(productEntity);
    }

    public static WishList toDto(WishListEntity wishListEntity) {
        return new WishList(wishListEntity.getMemberEntity().getId(), wishListEntity.getProductEntity().getPrice());
    }

}
