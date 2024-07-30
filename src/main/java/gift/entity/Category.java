package gift.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Schema(description = "카테고리")
@Entity
@Table(name = "category")
public class Category {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Schema(description = "카테고리 고유id")
    Long id;
    @Column(nullable = false, unique = true, length = 7)
    @Schema(description = "카테고리 이름")
    String name;
    @Column
    @Schema(description = "카테고리 색상")
    String color;
    @Column
    @Schema(description = "카테고리 설명")
    String description;
    @Column(nullable = false)
    @Schema(description = "카테고리 이미지 url")
    String imageUrl;

    public Category() {
    }

    public Category(Long id, String name, String color, String description, String imageUrl) {
        this.id = id;
        this.name = name;
        this.color = color;
        this.description = description;
        this.imageUrl = imageUrl;
    }

    public Category(String name, String color, String description, String imageUrl) {
        this(null, name, color, description, imageUrl);
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

    public String getDescription() {
        return description;
    }

    public String getImageUrl() {
        return imageUrl;
    }
}
