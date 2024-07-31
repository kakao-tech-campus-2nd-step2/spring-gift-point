package gift.wish.entity;

import gift.exception.CustomException;
import gift.exception.ErrorCode;
import gift.product.entity.Product;
import gift.product.option.entity.Option;
import gift.user.entity.User;
import gift.wish.dto.request.UpdateWishRequest;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Positive;

@Entity
@Table(name = "wishes")
public class Wish {

    private static final int DEFAULT_QUANTITY = 1;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    @Positive
    private Integer quantity;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false,
        foreignKey = @ForeignKey(name = "fk_wishes_user_id_ref_users_id"))
    private User user;

    @ManyToOne
    @JoinColumn(name = "product_id", nullable = false,
        foreignKey = @ForeignKey(name = "fk_wishes_product_id_ref_products_id"))
    private Product product;

    public Wish(User user, Product product, int quantity) {
        validateQuantity(quantity);
        this.user = user;
        this.product = product;
        this.quantity = quantity;
    }

    protected Wish() {
    }

    public Long getId() {
        return id;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public User getUser() {
        return user;
    }

    public Product getProduct() {
        return product;
    }

    public boolean existsProduct(Option option) {
        return this.product.equals(option.getProduct());
    }

    public void changeQuantity(UpdateWishRequest request) {
        this.quantity = request.quantity();
    }

    public boolean isQuantityZero() {
        return quantity <= 0;
    }

    public void validateQuantity(Integer quantity) {
        if (quantity <= 0) {
            throw new CustomException(ErrorCode.INVALID_WISH_QUANTITY);
        }
    }

}
