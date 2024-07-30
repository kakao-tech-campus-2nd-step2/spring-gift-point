package gift.product.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import gift.user.domain.User;
import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.List;

@Entity(name = "wishlist")
public class WishList {

    @Id
    @Column(name = "wishlist_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    @JsonIgnore
    private User user;

    @OneToOne(mappedBy = "wishList", cascade = CascadeType.ALL, orphanRemoval = true)
    private WishListProduct wishListProduct;

    private LocalDateTime createdAt;

    public WishList() {
    }

    public WishList(User user, LocalDateTime now) {
        this.user = user;
        this.createdAt = now;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public WishListProduct getWishListProduct() {
        return wishListProduct;
    }

    public void setWishListProduct(WishListProduct wishListProduct) {
        this.wishListProduct = wishListProduct;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public void clearProducts() {
        if (wishListProduct != null) {
            wishListProduct.setWishList(null);
            wishListProduct = null;
        }
    }

    public void deleteProduct() {
        if (wishListProduct != null) {
            wishListProduct.setWishList(null);
            wishListProduct = null;
        }
    }

    public void addWishListProduct(WishListProduct wishListProduct) {
        this.wishListProduct = wishListProduct;
    }
}
