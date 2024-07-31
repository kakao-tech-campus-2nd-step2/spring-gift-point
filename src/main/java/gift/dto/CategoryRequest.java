package gift.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;

public record CategoryRequest(
        @NotBlank(message = "Category name cannot be blank")
        String name,

        String color,

        @JsonProperty("image_url")
        String imageUrl,

        String description
) {}
