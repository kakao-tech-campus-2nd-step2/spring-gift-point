package gift.product.dto.request;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.hibernate.validator.constraints.Length;

public record NewOption(
    @NotNull
    @NotEmpty
    @Length(max = 50)
    String name,

    @Size(min = 1, max = 100_000_000)
    Integer quantity
) {

}
