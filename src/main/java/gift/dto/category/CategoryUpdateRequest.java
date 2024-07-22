package gift.dto.category;

import static gift.util.constants.GeneralConstants.REQUIRED_FIELD_MISSING;

import jakarta.validation.constraints.NotNull;

public record CategoryUpdateRequest(
    @NotNull(message = REQUIRED_FIELD_MISSING)
    String name,

    @NotNull(message = REQUIRED_FIELD_MISSING)
    String color,

    @NotNull(message = REQUIRED_FIELD_MISSING)
    String imageUrl,

    String description
) {

}
