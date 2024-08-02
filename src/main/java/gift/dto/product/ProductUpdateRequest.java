package gift.dto.product;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

public class ProductUpdateRequest {
    @NotBlank
    @Size(max = 15, message = "The product name must be less than 15 characters")
    private String name;

    @NotNull
    @Positive(message = "Price must be positive")
    private Integer price;

    @NotBlank
    private String imageUrl;
}
