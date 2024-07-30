package gift.product.domain;

import jakarta.persistence.*;

@Entity(name = "wishlist_product")
public class WishListProduct {

    @Id
    @Column(name = "wishlist_product_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "wishlist_id")
    private WishList wishList;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    private Product product;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_option_id")
    private ProductOption productOption;

    private Long quantity;

    public WishListProduct() {
    }

    public WishListProduct(WishList wishList, Product product, ProductOption productOption, Long quantity) {
        this.wishList = wishList;
        this.product = product;
        this.productOption = productOption;
        this.quantity = quantity;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public WishList getWishList() {
        return wishList;
    }

    public void setWishList(WishList wishList) {
        this.wishList = wishList;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }


    public ProductOption getProductOption() {
        return productOption;
    }

    public Long getQuantity() {
        return quantity;
    }
}
