package gift.dto;

import jakarta.persistence.Column;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import org.hibernate.validator.constraints.URL;

public class CategoryDTO {

    private Long id;

    @NotBlank(message = "카테고리 이름은 필수입니다")
    @Column(nullable = false)
    private String name;

    @NotBlank(message = "색상 코드는 필수입니다")
    @Pattern(regexp = "^#[0-9A-Fa-f]{6}$", message = "유효한 색상 코드가 아닙니다")
    @Column(nullable = false)
    private String color;

    @NotNull
    @URL(message = "유효한 URL 형식이 아닙니다")
    @Column(name = "image_url", nullable = false)
    private String imageUrl;

    private String description;

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

    public void setId(Long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
