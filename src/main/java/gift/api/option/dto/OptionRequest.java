package gift.api.option.dto;

import gift.api.option.domain.Option;
import gift.api.product.domain.Product;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record OptionRequest(
    @NotBlank(message = "Name is mandatory")
    @Size(max = 50, message = "Name must be less than or equal to 50 characters long")
    @Pattern(regexp = "^[\\w\\s가-힣()\\[\\]+\\-&/]+$", message = "Only (), [], +, -, &, / of special characters available")
    String name,
    @NotNull
    @Min(value = 1)
    @Max(value = 100_000_000)
    Integer quantity
) {
    public Option toEntity(Product product) {
        return new Option(product, name, quantity);
    }
}
