package gift.web.dto.category;

import jakarta.validation.constraints.NotNull;

public record CategoryRequestDto(
    @NotNull
    String name,

    @NotNull
    String color,

    @NotNull
    String imageUrl,

    String description
) {

}
