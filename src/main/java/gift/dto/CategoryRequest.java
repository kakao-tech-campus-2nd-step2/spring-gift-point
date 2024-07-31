package gift.dto;

import jakarta.validation.constraints.NotBlank;

public record CategoryRequest(
        @NotBlank(message = "Category name cannot be blank")
        String name,

        String color,

        String imageUrl,

        String description
) {}
