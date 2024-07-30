package gift.product.domain;

import gift.user.domain.User;
import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity(name = "wishlist")
public class WishList {

    @Id
    @Column(name = "wishlist_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(mappedBy = "wishList")
    private List<WishListProduct> wishListProducts = new ArrayList<>();

    private LocalDateTime createdAt;

    public WishList() {
    }


    public <E> WishList(User user, LocalDateTime now) {
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
        user.getWishLists().add(this);
    }

    public List<WishListProduct> getWishListProducts() {
        return wishListProducts;
    }

    public void addWishListProduct(WishListProduct wishListProduct) {
        wishListProducts.add(wishListProduct);
        wishListProduct.setWishList(this);
    }

    public void removeWishListProduct(Long wishListProductId) {
        wishListProducts.removeIf(wishListProduct -> wishListProduct.getId().equals(wishListProductId));
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public void clearProducts() {
        wishListProducts.clear();
    }

    public void deleteProduct(Long optionId) {
        wishListProducts.removeIf(wishListProduct -> wishListProduct.getProductOption().getId().equals(optionId));
    }
}
