package gift.dto.requestdto;

import gift.domain.Category;
import jakarta.validation.constraints.NotNull;

public record CategoryRequestDTO(
    @NotNull
    String name,
    @NotNull
    String color,
    @NotNull
    String imageUrl,
    String description) {

    public Category toEntity() {
        return new Category(name, color, imageUrl, description);
    }
}
