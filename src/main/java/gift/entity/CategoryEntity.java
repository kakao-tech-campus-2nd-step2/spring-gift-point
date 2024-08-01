package gift.entity;

import gift.dto.CategoryDTO;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.util.List;
import java.util.regex.Pattern;

@Entity
@Table(name = "category")
public class CategoryEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, length = 7)
    private String color;

    @Column(name = "image_url", nullable = false)
    private String imageUrl;

    @Column(nullable = false)
    private String description;

    @OneToMany(mappedBy = "category", fetch = FetchType.LAZY,
        cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ProductEntity> productEntities;

    public CategoryEntity() {}

    public CategoryEntity(String name, String color, String imageUrl, String description) {
        this.name = name;
        this.color = color;
        this.imageUrl = imageUrl;
        this.description = description;
    }

    public CategoryEntity(CategoryDTO categoryDTO) {
        this.id = categoryDTO.getId();
        this.name = categoryDTO.getName();
        this.color = categoryDTO.getColor();
        this.imageUrl = categoryDTO.getImageUrl();
        this.description = categoryDTO.getDescription();
    }

    public CategoryEntity(Long id, String name, String color, String imageUrl, String description) {
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

    public static CategoryDTO toDTO(CategoryEntity category) {
        return new CategoryDTO(category.getId(), category.getName(), category.getColor(), category.getImageUrl(), category.getDescription());
    }

    public void update(String name, String color, String imageUrl, String description) {
        if (name == null || name.trim().isBlank()) {
            throw new IllegalArgumentException("이름은 필수 입력 사항입니다.");
        }

        if (!isColorValidation(color)) {
            throw new IllegalArgumentException("색상 값은 3자리 또는 6자리의 헥스코드여야 합니다.");
        }

        if (!isImageUrlValidation(imageUrl)) {
            throw new IllegalArgumentException("이미지 URL이 유효하지 않습니다.");
        }

        if(description.length() > 255) {
            throw new IllegalArgumentException("설명은 255자를 넘길 수 없습니다.");
        }

        this.name = name;
        this.color = color;
        this.imageUrl = imageUrl;
        this.description = description;
    }

    private boolean isColorValidation(String color) {
        return Pattern.matches("^#([A-Fa-f0-9]{6}|[A-Fa-f0-9]{3})$", color);
    }

    private boolean isImageUrlValidation(String imageUrl) {
        return Pattern.matches("^https?://.*$", imageUrl);
    }
}
