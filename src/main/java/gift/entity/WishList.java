package gift.entity;

import gift.dto.wish.ResponseWishDTO;
import jakarta.persistence.*;

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

    public ResponseWishDTO toResponseDTO(){
        return new ResponseWishDTO(id,product.toResponseDTO());
    }
}
