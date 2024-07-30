package gift.dto;

import gift.domain.Category;
import jakarta.validation.constraints.NotNull;

public record CategoryDTO(
    long id,

    @NotNull
    String name,

    @NotNull
    String color,

    @NotNull
    String imageUrl,

    @NotNull
    String description
) {

    public Category toEntity() {
        return new Category(id, name, color, imageUrl, description);
    }
}
