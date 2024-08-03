package gift.dto.wishedProduct;

import jakarta.validation.constraints.NotNull;

public record AddWishedProductRequest(
    @NotNull
    long productId
) {

}
