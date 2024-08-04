package gift.wishedProduct.dto;

import jakarta.validation.constraints.NotNull;

public record AddWishedProductRequest(
    @NotNull
    long productId
) {

}
