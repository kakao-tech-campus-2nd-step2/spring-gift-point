package gift.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import gift.dto.category.CategoryRequest;
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
    @JsonProperty("image_url")
    private String imageUrl;
    @Schema(description = "카테고리 설명", nullable = false, example = "카테고리 설명 입니다.")
    private String description;

    public Category() {
    }

    public Category(Long id, String name, String color, String imageUrl, String description) {
        this.id = id;
        this.name = name;
        this.color = color;
        this.imageUrl = imageUrl;
        this.description = description;
    }

    public Category(CategoryRequest categoryRequest) {
        this.name = categoryRequest.getName();
        this.color = categoryRequest.getColor();
        this.imageUrl = categoryRequest.getImage_url();
        this.description = categoryRequest.getDescription();
    }

    public void setCategory(CategoryRequest categoryRequest) {
        this.name = categoryRequest.getName();
        this.color = categoryRequest.getColor();
        this.imageUrl = categoryRequest.getImage_url();
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

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String image_url) {
        this.imageUrl = image_url;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
