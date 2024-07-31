package gift.dto.request;

import gift.dto.CategoryDto;
import jakarta.validation.constraints.NotEmpty;
import org.hibernate.validator.constraints.Length;

public class CategoryRequest {

    @Length(max = 15)
    @NotEmpty
    private String name;

    @NotEmpty
    private String color;

    @NotEmpty
    private String imageUrl;

    private String description;

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

    public CategoryDto toDto() {
        return new CategoryDto(this.name, this.color, this.imageUrl, this.description);
    }

}
