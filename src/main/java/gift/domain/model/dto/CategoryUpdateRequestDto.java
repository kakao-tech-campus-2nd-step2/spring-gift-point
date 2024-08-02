package gift.domain.model.dto;

import gift.domain.model.entity.Category;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.hibernate.validator.constraints.URL;

public class CategoryUpdateRequestDto {

    @NotBlank
    private final String name;

    @NotBlank
    private final String color;

    @NotBlank
    @URL
    private final String imageUrl;

    @NotBlank
    @Size(max = 255)
    private final String description;

    public CategoryUpdateRequestDto(String name, String color, String imageUrl, String description) {
        this.name = name;
        this.color = color;
        this.imageUrl = imageUrl;
        this.description = description;
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

    public Category toEntity() {
        return new Category(this.name, this.color, this.imageUrl, this.description);
    }
}
