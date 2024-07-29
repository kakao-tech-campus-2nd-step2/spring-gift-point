package gift.domain;

import jakarta.persistence.*;

@Entity
@Table(name = "wishList")
public class WishProduct extends BaseEntity{
    @ManyToOne
    @JoinColumn(
            name = "user_id",
            foreignKey = @ForeignKey(name = "fk_wish_product_id_ref_product_id"),
            nullable = false)
    private User user;
    @ManyToOne
    @JoinColumn(
            name = "product_id",
            foreignKey = @ForeignKey(name = "fk_wish_user_id_ref_user_id"),
            nullable = false)
    private Product product;


    protected WishProduct(){
        super();
    }

    public WishProduct(User user, Product product) {
        this.user = user;
        this.product = product;
    }

    public Long getId(){
        return super.getId();
    }
    public User getUser() {
        return user;
    }

    public Product getProduct() {
        return product;
    }
}
