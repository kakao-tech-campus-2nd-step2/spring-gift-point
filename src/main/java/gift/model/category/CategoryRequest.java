package gift.model.category;

import jakarta.validation.constraints.NotBlank;

public record CategoryRequest(
    @NotBlank
    String name,
    String color,
    String imageUrl,
    String description) {


}
