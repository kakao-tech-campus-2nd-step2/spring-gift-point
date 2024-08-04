package gift.model.category;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;

@Entity
@Table(name = "category")
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "카테고리 id", nullable = false, example = "1")
    private Long id;
    @Schema(description = "카테고리명", nullable = false, example = "전자기기")
    private String name;
    @Schema(description = "카테고리 색상", nullable = false, example = "#FFFFFF")
    private String color;
    @Schema(description = "카테고리 이미지 url", nullable = false, example = "https://www.test.com")
    private String image_url;
    @Schema(description = "카테고리 설명", nullable = false, example = "카테고리 설명 입니다.")
    private String description;

    public Category() {
    }

    public Category(Long id, String name, String color, String image_url, String description) {
        this.id = id;
        this.name = name;
        this.color = color;
        this.image_url = image_url;
        this.description = description;
    }

    public Category(CategoryRequest categoryRequest) {
        this.name = categoryRequest.getName();
        this.color = categoryRequest.getColor();
        this.image_url = categoryRequest.getImage_url();
        this.description = categoryRequest.getDescription();
    }

    public void setCategory(CategoryRequest categoryRequest) {
        this.name = categoryRequest.getName();
        this.color = categoryRequest.getColor();
        this.image_url = categoryRequest.getImage_url();
        this.description = categoryRequest.getDescription();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getImage_url() {
        return image_url;
    }

    public void setImage_url(String image_url) {
        this.image_url = image_url;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
