package gift.controller.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

public class CategoryRequest {

    public record CreateCategory(
            @NotBlank
            String name,
            @NotBlank
            String color,
            @NotBlank
            String imageUrl,
            String description
    ) {
    }

    public record UpdateCategory(
            @Min(1)
            Long id,
            @NotBlank
            String name,
            @NotBlank
            String color,
            @NotBlank
            String imageUrl,
            String description
    ) {
    }
}
