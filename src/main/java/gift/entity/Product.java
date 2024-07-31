package gift.entity;

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
    private String imageurl;
    @Schema(description = "카테고리 id", nullable = false, example = "1")
    private Long category_id;

    @ManyToOne
    @JoinColumn(name = "user_id")
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
        this.imageurl = product.getImageurl();
        this.category_id = product.getCategoryid();
    }

    public Product(ProductDTO product, User user) {
        this.name = product.getName();
        this.price = product.getPrice();
        this.imageurl = product.getImageurl();
        this.category_id = product.getCategoryid();
        this.user = user;
    }

    public Product(String name, int price, String imageurl, Long categoryid) {
        this.name = name;
        this.price = price;
        this.imageurl = imageurl;
        this.category_id = categoryid;
    }

    public void updateProduct(ProductDTO product) {
        this.name = product.getName();
        this.price = product.getPrice();
        this.imageurl = product.getImageurl();
        this.category_id = product.getCategoryid();
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

    public String getImageurl() {
        return imageurl;
    }

    public Long getCategoryid() {
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

    public void setImageurl(String imageurl) {
        this.imageurl = imageurl;
    }

    public void setCategoryid(Long categoryid) {
        this.category_id = categoryid;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public User getUser() {
        return user;
    }
}
