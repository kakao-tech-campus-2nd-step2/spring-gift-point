package gift.entity;

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
    private String imageurl;
    @Schema(description = "카테고리 설명", nullable = false, example = "카테고리 설명 입니다.")
    private String description;

    public Category() {
    }

    public Category(Long id, String name, String color, String imageurl, String description) {
        this.id = id;
        this.name = name;
        this.color = color;
        this.imageurl = imageurl;
        this.description = description;
    }

    public Category(CategoryDTO categoryDTO) {
        this.name = categoryDTO.getName();
        this.color = categoryDTO.getColor();
        this.imageurl = categoryDTO.getImageurl();
        this.description = categoryDTO.getDescription();
    }

    public void setCategory(CategoryDTO categoryDTO) {
        this.name = categoryDTO.getName();
        this.color = categoryDTO.getColor();
        this.imageurl = categoryDTO.getImageurl();
        this.description = categoryDTO.getDescription();
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

    public String getImageurl() {
        return imageurl;
    }

    public void setImageurl(String imageurl) {
        this.imageurl = imageurl;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
