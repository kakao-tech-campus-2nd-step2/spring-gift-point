package gift.model;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public record OrderDTO(
    long id,
    @NotNull long optionId,
    @NotNull long quantity,
    String orderDate,
    @NotEmpty String message) {
}
