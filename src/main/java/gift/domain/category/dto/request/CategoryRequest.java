package gift.domain.category.dto.request;

import gift.domain.category.Category;
import jakarta.validation.constraints.NotBlank;
import org.hibernate.validator.constraints.URL;

public record CategoryRequest(
    @NotBlank
    String name,

    @NotBlank
    String description,

    @NotBlank
    String color,

    @NotBlank
    @URL
    String imageUrl
) {

    public Category toCategory() {
        return new Category(this.name, this.description, this.color, this.imageUrl);
    }
}
