package gift.entity;

import jakarta.persistence.*;

import java.io.Serializable;
import java.util.Objects;

@Entity
public class WishList {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int id;

    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    private Product product;

    public WishList() {
    }

    public WishList(User user, Product product) {
        this.user = user;
        user.addWishlist(this);
        this.product = product;
        product.addWishlist(this);
    }

    public Product getProduct() {
        return product;
    }

    public User getUser() {
        return user;
    }

    public int getId() {
        return id;
    }
}
