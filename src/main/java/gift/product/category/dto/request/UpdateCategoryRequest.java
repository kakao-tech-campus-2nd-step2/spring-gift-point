package gift.product.category.dto.request;

import com.fasterxml.jackson.databind.PropertyNamingStrategies.SnakeCaseStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

@JsonNaming(SnakeCaseStrategy.class)
public record UpdateCategoryRequest(
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
