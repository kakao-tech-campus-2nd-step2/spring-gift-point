package gift.dto.wish;

import static gift.util.constants.GeneralConstants.REQUIRED_FIELD_MISSING;

import jakarta.validation.constraints.NotNull;

public record WishCreateRequest(
    @NotNull(message = REQUIRED_FIELD_MISSING)
    Long productId
) {

}
