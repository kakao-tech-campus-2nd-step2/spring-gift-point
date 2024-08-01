package gift.product.category.dto.request;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public record CreateCategoryRequest(
    @NotNull
    @NotEmpty
    String name,

    @NotNull
    String color,

    @NotNull
    String imageUrl,

    String description
) {

}
