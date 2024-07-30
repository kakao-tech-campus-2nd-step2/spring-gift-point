package gift.entity;

import gift.exception.BusinessException;
import gift.exception.ErrorCode;
import jakarta.persistence.*;

@Entity
@Table(name = "product")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Embedded
    private ProductName name;

    @Column(nullable = false, columnDefinition = "INT NOT NULL COMMENT 'Product Price'")
    private int price;

    @Column(name = "image_url", nullable = false, columnDefinition = "VARCHAR(255) NOT NULL COMMENT 'Product Image URL'")
    private String imageUrl;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", nullable = false, foreignKey = @ForeignKey(name = "fk_product_category"), columnDefinition = "BIGINT NOT NULL COMMENT 'Foreign Key to Category'")
    private Category category;

    protected Product() {
    }

    public Product(Long id, ProductName name, int price, String imageUrl, Category category) {
        validatePrice(price);
        validateImageUrl(imageUrl);
        this.id = id;
        this.name = name;
        this.price = price;
        this.imageUrl = imageUrl;
        this.category = category;
    }

    public Product(ProductName name, int price, String imageUrl, Category category) {
        this(null, name, price, imageUrl, category);
    }

    public void update(ProductName name, int price, String imageUrl, Category category) {
        validatePrice(price);
        validateImageUrl(imageUrl);
        this.name = name;
        this.price = price;
        this.imageUrl = imageUrl;
        this.category = category;
    }

    private void validatePrice(int price) {
        if (price < 0) {
            throw new BusinessException(ErrorCode.INVALID_PRICE);
        }
    }

    private void validateImageUrl(String imageUrl) {
        if (imageUrl == null || imageUrl.trim().isEmpty()) {
            throw new BusinessException(ErrorCode.INVALID_IMAGE_URL);
        }
    }

    public Long getId() {
        return id;
    }

    public ProductName getName() {
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
}
