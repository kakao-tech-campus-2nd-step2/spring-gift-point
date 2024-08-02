package gift.dto.category;

import gift.domain.Category;
import jakarta.validation.constraints.NotNull;

public record CategoryDto(
    long id,

    @NotNull
    String name,

    @NotNull
    String imageUrl,

    @NotNull
    String description
) {

    public Category toEntity() {
        return new Category(id, name, imageUrl, description);
    }
}
