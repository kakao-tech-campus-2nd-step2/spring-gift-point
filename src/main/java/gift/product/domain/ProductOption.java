package gift.product.domain;

import jakarta.persistence.*;

@Entity(name = "product_option")
public class ProductOption {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;

    private Long quentity;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    private Product product;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "wishlist_product_id")
    private WishListProduct wishListProduct;

    public ProductOption(String name, Long quentity, Product product) {
        this.name = name;
        this.quentity = quentity;
        product.addProductOption(this);
    }

    public ProductOption() {
    }

    public Long getId() {
        return id;
    }

    public Long getQuentity() {
        return quentity;
    }

    public String getName() {
        return name;
    }

    public void decreaseQuantity(Long quentity) {
        if (this.quentity < quentity) {
            throw new IllegalArgumentException("재고가 부족합니다.");
        }
        this.quentity -= quentity;
    }
}
