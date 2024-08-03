package gift.entity.product;

import com.fasterxml.jackson.annotation.JsonIgnore;
import gift.entity.middle.ProductOption;
import gift.entity.middle.ProductWishlist;
import gift.entity.user.User;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "product")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "상품 id", nullable = false, example = "1")
    private Long id;
    @Schema(description = "상품명", nullable = false, example = "상품명 입니다")
    private String name;
    @Schema(description = "상품 가격", nullable = false, example = "10000")
    private int price;
    @Schema(description = "상품 이미지 url", nullable = false, example = "https://www.test.com")
    private String imageUrl;
    @Schema(description = "카테고리 id", nullable = false, example = "1")
    private Long category_id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    @JsonIgnore
    private User user;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<ProductWishlist> productWishlist = new ArrayList<>();

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<ProductOption> productOption = new ArrayList<>();

    public Product() {
    }

    public Product(ProductDTO product) {
        this.name = product.getName();
        this.price = product.getPrice();
        this.imageUrl = product.getImageUrl();
        this.category_id = product.getCategory_id();
    }

    public Product(ProductDTO product, User user) {
        this.name = product.getName();
        this.price = product.getPrice();
        this.imageUrl = product.getImageUrl();
        this.category_id = product.getCategory_id();
        this.user = user;
    }

    public Product(String name, int price, String imageUrl, Long category_id) {
        this.name = name;
        this.price = price;
        this.imageUrl = imageUrl;
        this.category_id = category_id;
    }

    public void updateProduct(ProductDTO product) {
        this.name = product.getName();
        this.price = product.getPrice();
        this.imageUrl = product.getImageUrl();
        this.category_id = product.getCategory_id();
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public int getPrice() {
        return price;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public Long getCategory_id() {
        return category_id;
    }

    public Long getId() {
        return id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public void setCategory_id(Long category_id) {
        this.category_id = category_id;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public User getUser() {
        return user;
    }
}
