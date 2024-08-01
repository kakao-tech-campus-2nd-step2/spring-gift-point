package gift.DTO.category;

import gift.domain.Category;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public class CategoryRequest {

    @NotBlank(message = "Please enter the category name")
    private final String name;

    @NotBlank
    @Pattern(regexp = "^#([0-9a-fA-F]{6})$", message = "Invalid color format")
    private final String color;

    @NotBlank
    @Pattern(
        regexp = "^https?://[\\w.-]+(?:\\.[\\w\\.-]+)+/.*\\.(jpg|jpeg|png|gif|bmp)$",
        message = "Invalid URL format"
    )
    private final String imageUrl;

    private final String description;

    public CategoryRequest(String name, String color, String imageUrl, String description) {
        this.name = name;
        this.color = color;
        this.imageUrl = imageUrl;
        this.description = description;
    }

    public Category toEntity() {
        return new Category(name, color, imageUrl, description);
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
}
