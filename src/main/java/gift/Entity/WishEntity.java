package gift.Entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
public class WishEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity user;

    @ManyToOne
    @JoinColumn(name = "product_id", nullable = false)
    private ProductEntity product;

    private LocalDateTime createdAt = LocalDateTime.now();

    private String productName;

    public WishEntity() {}

    public WishEntity(Long id, UserEntity user, ProductEntity product, String productName) {
        this.id = id;
        this.user = user;
        this.product = product;
        this.productName = productName;
    }

    public WishEntity(UserEntity user, ProductEntity product) {
        this.user = user;
        this.product = product;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public UserEntity getUser() {
        return user;
    }

    public void setUser(UserEntity user) {
        this.user = user;
    }

    public ProductEntity getProduct() {
        return product;
    }

    public void setProduct(ProductEntity product) {
        this.product = product;
    }

    public String getProductName() {
        return productName;
    }
}
