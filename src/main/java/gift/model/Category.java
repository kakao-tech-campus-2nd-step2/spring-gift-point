package gift.model;

import gift.exception.InputException;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

@Entity
@Table(uniqueConstraints = {
    @UniqueConstraint(
        name = "uk_category",
        columnNames = {"name"}
    )
})
public class Category extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "category_id")
    private Long id;
    @Column(nullable = false)
    private String name;
    @Column(nullable = false)
    private String color;
    @Column(nullable = false)
    private String imageUrl;
    @Column(nullable = false)
    private String description;

    protected Category() {
    }

    public Category(Long id, String name) {
        validateName(name);
        this.id = id;
        this.name = name;
    }

    public Category(Long id, String name, String color, String imageUrl, String description) {
        validateName(name);
        validateColor(color);
        validateImageUrl(imageUrl);
        validateDescription(description);
        this.id = id;
        this.name = name;
        this.color = color;
        this.imageUrl = imageUrl;
        this.description = description;
    }

    public Category(String name, String color, String imageUrl, String description) {
        validateName(name);
        validateColor(color);
        validateImageUrl(imageUrl);
        validateDescription(description);
        this.name = name;
        this.color = color;
        this.imageUrl = imageUrl;
        this.description = description;
    }

    public Category(String name) {
        this.name = name;
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

    public void updateCategory(String newName) {
        validateName(newName);
        this.name = newName;
    }

    public void updateCategory(String newName, String newColor, String newImageUrl, String newDescription) {
        validateName(newName);
        validateColor(newColor);
        validateImageUrl(newImageUrl);
        validateDescription(newDescription);
        this.name = newName;
        this.color = newColor;
        this.imageUrl = newImageUrl;
        this.description = newDescription;
    }

    private void validateName(String name) {
        if (name == null || name.isEmpty() || name.length() > 20) {
            throw new InputException("이름을 1~20자 사이로 입력해주세요");
        }
    }

    private void validateColor(String color) {
        if (color == null || color.isEmpty()) {
            throw new InputException("색상을 입력해주세요");
        }
    }

    private void validateImageUrl(String imageUrl) {
        if (imageUrl == null || imageUrl.isEmpty()) {
            throw new InputException("이미지 주소를 입력해주세요.");
        }
    }

    private void validateDescription(String description) {
        if (description == null || description.isEmpty()) {
            throw new InputException("설명을 입력해주세요.");
        }
    }


}
