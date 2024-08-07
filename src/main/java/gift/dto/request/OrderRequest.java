package gift.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import static gift.constant.ErrorMessage.POSITIVE_NUMBER_REQUIRED_MSG;
import static gift.constant.ErrorMessage.REQUIRED_FIELD_MSG;

public record OrderRequest(
        @NotNull(message = REQUIRED_FIELD_MSG)
        Long optionId,

        @Positive(message = POSITIVE_NUMBER_REQUIRED_MSG)
        @NotNull(message = REQUIRED_FIELD_MSG)
        Integer quantity,

        @NotBlank(message = REQUIRED_FIELD_MSG)
        String message) {
}
