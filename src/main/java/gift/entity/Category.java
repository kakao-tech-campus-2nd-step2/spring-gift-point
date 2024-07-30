package gift.entity;

import gift.exception.BusinessException;
import gift.exception.ErrorCode;
import jakarta.persistence.*;

@Entity
@Table(name = "category")
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, columnDefinition = "VARCHAR(255) NOT NULL COMMENT 'Category Name'")
    private String name;

    @Column(nullable = false, columnDefinition = "VARCHAR(7) NOT NULL COMMENT 'Category Color'")
    private String color;

    @Column(name = "image_url", nullable = false, columnDefinition = "VARCHAR(255) NOT NULL COMMENT 'Category Image URL'")
    private String imageUrl;

    @Column(columnDefinition = "TEXT COMMENT 'Category Description'")
    private String description;

    protected Category() {
    }

    public Category(String name, String color, String imageUrl, String description) {
        this(null, name, color, imageUrl, description);
    }

    public Category(Long id, String name, String color, String imageUrl, String description) {
        validateName(name);
        validateColor(color);
        validateImageUrl(imageUrl);
        this.id = id;
        this.name = name;
        this.color = color;
        this.imageUrl = imageUrl;
        this.description = description;
    }

    public void update(String name, String color, String imageUrl, String description) {
        validateName(name);
        validateColor(color);
        validateImageUrl(imageUrl);
        this.name = name;
        this.color = color;
        this.imageUrl = imageUrl;
        this.description = description;
    }

    private void validateName(String name) {
        if (name == null || name.trim().isEmpty()) {
            throw new BusinessException(ErrorCode.INVALID_CATEGORY_NAME);
        }
    }

    private void validateColor(String color) {
        if (color == null || color.trim().isEmpty()) {
            throw new BusinessException(ErrorCode.INVALID_CATEGORY_COLOR);
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

    public String getName() {
        return name;
    }

    public String getColor() {
        return color;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public String getDescription() {
        return description;
    }
}
