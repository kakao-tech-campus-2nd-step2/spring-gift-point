package gift.product.option.dto.request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Length;

public record CreateOptionRequest(
    @NotNull
    @NotEmpty
    @Length(max = 50)
    String name,

    @Min(value = 1)
    @Max(value = 100_000_000 - 1)
    Integer quantity
) {

}
