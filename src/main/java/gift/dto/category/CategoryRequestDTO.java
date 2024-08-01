package gift.dto.category;

import jakarta.validation.constraints.NotNull;

public record CategoryRequestDTO(@NotNull String name,
                                 @NotNull String color,
                                 @NotNull String imageUrl,
                                 String description) {
}
