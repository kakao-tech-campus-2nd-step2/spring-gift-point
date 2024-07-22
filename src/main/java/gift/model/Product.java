package gift.model;

import static gift.util.constants.ProductConstants.NAME_INVALID_CHARACTERS;
import static gift.util.constants.ProductConstants.NAME_REQUIRES_APPROVAL;
import static gift.util.constants.ProductConstants.NAME_SIZE_LIMIT;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "product")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 15)
    @Size(min = 1, max = 15, message = NAME_SIZE_LIMIT)
    @Pattern(
        regexp = "^[a-zA-Z0-9ㄱ-ㅎ가-힣\\(\\)\\[\\]\\+\\-\\&\\/\\_ ]*$",
        message = NAME_INVALID_CHARACTERS
    )
    @Pattern(
        regexp = "^(?!.*카카오).*$",
        message = NAME_REQUIRES_APPROVAL
    )
    private String name;

    @Column(nullable = false)
    private int price;

    @Column(nullable = false, name = "image_url", length = 255)
    private String imageUrl;

    @ManyToOne
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;

    public Product() {
    }

    public Product(Long id, String name, int price, String imageUrl, Category category) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.imageUrl = imageUrl;
        this.category = category;
    }

    public Product(String name, int price, String imageUrl, Category category) {
        this.name = name;
        this.price = price;
        this.imageUrl = imageUrl;
        this.category = category;
    }

    public Long getId() {
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

    public Long getCategoryId() {
        return category.getId();
    }

    public String getCategoryName() {
        return category.getName();
    }

    public void update(String name, int price, String imageUrl, Category category) {
        this.name = name;
        this.price = price;
        this.imageUrl = imageUrl;
        this.category = category;
    }
}
