package gift.dto.category;

import gift.domain.Category;
import jakarta.validation.constraints.NotNull;

public record CategoryDto(
    long categoryId,

    @NotNull
    String name,

    @NotNull
    String imageUrl,

    @NotNull
    String description
) {

    public Category toEntity() {
        return new Category(categoryId, name, imageUrl, description);
    }
}
