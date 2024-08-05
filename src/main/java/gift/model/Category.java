package gift.model;

import gift.exception.InvalidInputValueException;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "category")
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false, unique = true)
    private String name;

    @Column(name = "color", nullable = false)
    private String color;

    @Column(name = "image_url", nullable = false)
    private String imageUrl;

    @Column(name = "description")
    private String description;

    protected Category() {
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

    public void updateCategory(String name, String color, String imageUrl, String description) {
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
            throw new InvalidInputValueException("카테고리 이름을 입력하세요.");
        }
    }

    private void validateColor(String color) {
        if (color == null || color.trim().isEmpty()) {
            throw new InvalidInputValueException("카테고리 색상을 입력하세요");
        }
    }

    private void validateImageUrl(String imageUrl) {
        if (imageUrl == null || imageUrl.trim().isEmpty()) {
            throw new InvalidInputValueException("이미지 URL을 입력하세요");
        }
    }

}
