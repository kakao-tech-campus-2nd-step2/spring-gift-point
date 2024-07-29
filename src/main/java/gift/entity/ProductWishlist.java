package gift.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "product_wishlist")
public class ProductWishlist {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;

    @ManyToOne
    @JoinColumn(name = "wishlist_id")
    private Wishlist wishlist;

    public ProductWishlist(Product product, Wishlist wishlist) {
        this.product = product;
        this.wishlist = wishlist;
    }

    public ProductWishlist() {

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Product getProduct() {
        return product;
    }

    public Wishlist getWishlist() {
        return wishlist;
    }
}
