package gift.dto.option;

import jakarta.validation.constraints.*;

public class OptionCreateRequest {
    @NotBlank
    @Size(max = 50, message = "The option name must be less than 50 characters")
    private String name;

    @NotNull
    @Min(value = 1, message = "Quantity must be greater than 1")
    @Max(value = 99_999_999, message = "Quantity must be less than 100 million..")
    private Long quantity;
}
