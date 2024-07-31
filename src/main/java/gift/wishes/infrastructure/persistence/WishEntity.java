package gift.wishes.infrastructure.persistence;

import gift.core.BaseEntity;
import gift.product.infrastructure.persistence.entity.ProductEntity;
import gift.user.infrastructure.persistence.entity.UserEntity;
import gift.wishes.service.WishDto;
import jakarta.persistence.*;

@Entity
@Table(name = "wish")
public class WishEntity extends BaseEntity {
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity user;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "product_id", nullable = false)
    private ProductEntity product;

    @Column(name = "quantity", nullable = false)
    private Long quantity;

    protected WishEntity() {
    }

    protected WishEntity(
            Long id,
            UserEntity user,
            ProductEntity product,
            Long quantity
    ) {
        super(id);
        this.user = user;
        this.product = product;
    }

    protected WishEntity(UserEntity user, ProductEntity product, Long quantity) {
        this.user = user;
        this.product = product;
    }

    public static WishEntity of(UserEntity user, ProductEntity product, Long quantity) {
        return new WishEntity(user, product, quantity);
    }

    public UserEntity getUser() {
        return user;
    }

    public WishDto toDto() {
        return new WishDto(
                getId(),
                product.getName(),
                product.getPrice(),
                product.getImageUrl(),
                quantity
        );
    }
}
