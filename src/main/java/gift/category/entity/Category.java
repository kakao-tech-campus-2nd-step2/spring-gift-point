package gift.category.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import java.util.Objects;

@Entity
@Schema(description = "카테고리")
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "카테고리 id")
    private long id;

    @Column(unique = true, nullable = false)
    @Schema(description = "카테고리명")
    private String name;

    @Column(nullable = false)
    @Schema(description = "카테고리 색상")
    private String color;

    @Column(nullable = false)
    @Schema(description = "카테고리 이미지 url")
    private String imageUrl;

    @Column(nullable = false)
    @Schema(description = "카테고리 설명")
    private String description;

    protected Category() {
    }

    public Category(
        long id,
        String name,
        String color,
        String imageUrl,
        String description
    ) {
        this.id = id;
        this.name = name;
        this.color = color;
        this.imageUrl = imageUrl;
        this.description = description;
    }

    public long getId() {
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

    @Override
    public boolean equals(Object object) {
        if (object instanceof Category category) {
            return id == category.id
                   && Objects.equals(name, category.name)
                   && Objects.equals(color, category.color)
                   && Objects.equals(imageUrl, category.imageUrl)
                   && Objects.equals(description, category.description);
        }
        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, color, imageUrl, description);
    }
}
