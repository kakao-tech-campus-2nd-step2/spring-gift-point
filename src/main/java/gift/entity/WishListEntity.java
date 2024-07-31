package gift.entity;

import gift.dto.WishListDTO;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name="wishlist")
public class WishListEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "product_id", nullable = false)
    private ProductEntity productEntity;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private MemberEntity userEntity;

    @ManyToOne
    @JoinColumn(name = "option_id", nullable = true)  // 옵션 엔티티와의 관계 추가
    private OptionEntity optionEntity;

    public WishListEntity() {}

    public WishListEntity(ProductEntity productEntity, MemberEntity userEntity) {
        this.productEntity = productEntity;
        this.userEntity = userEntity;
    }

    public WishListEntity(ProductEntity productEntity, MemberEntity userEntity, OptionEntity optionEntity) {
        this.productEntity = productEntity;
        this.userEntity = userEntity;
        this.optionEntity = optionEntity;
    }

    public ProductEntity getProductEntity() {
        return productEntity;
    }

    public MemberEntity getUserEntity() {
        return userEntity;
    }

    public OptionEntity getOptionEntity() {
        return optionEntity;
    }

    public static WishListDTO toDTO(WishListEntity wishListEntity) {
        return new WishListDTO(wishListEntity.getProductEntity().getId(), wishListEntity.getUserEntity().getId());
    }
}
