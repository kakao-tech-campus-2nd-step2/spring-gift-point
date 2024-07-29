package gift.product.entity;

import gift.category.Category;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity
@Schema(description = "상품")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "상품 id")
    private long id;

    @Column(nullable = false, length = 15)
    @Schema(description = "상품명")
    private String name;

    @Column(nullable = false)
    @Schema(description = "상품 가격")
    private int price;

    @Column(nullable = false)
    @Schema(description = "상품 이미지 url")
    private String imageUrl;

    @JoinColumn(name = "category", nullable = false)
    @ManyToOne
    @Schema(description = "카테고리")
    private Category category;

    public long getId() {
        return id;
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

    public Category getCategory() {
        return category;
    }

    protected Product() {
    }

    public Product(long id, String name, int price, String imageUrl, Category category) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.imageUrl = imageUrl;
        this.category = category;
    }

    public Product(ProductDTO productDTO, Category category) {
        this(
            -1L,
            productDTO.name(),
            productDTO.price(),
            productDTO.imageUrl(),
            category
        );
    }

    public void update(String name, int price, String imageUrl, Category category) {
        this.name = name;
        this.price = price;
        this.imageUrl = imageUrl;
        this.category = category;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Product product) {
            return this.id == product.id;
        }
        return false;
    }

    @Override
    public int hashCode() {
        return (int) this.id;
    }
}
