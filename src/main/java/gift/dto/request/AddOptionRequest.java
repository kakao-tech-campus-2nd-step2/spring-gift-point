package gift.dto.request;

import jakarta.validation.constraints.*;

import static gift.constant.ErrorMessage.*;

public record AddOptionRequest(
        @NotBlank(message = REQUIRED_FIELD_MSG)
        @Size(max = 50, message = LENGTH_ERROR_MSG)
        @Pattern(regexp = "^[a-zA-Z0-9가-힣 ()\\[\\]+\\-&/_]*$", message = SPECIAL_CHAR_ERROR_MSG)
        String name,

        @NotNull(message = REQUIRED_FIELD_MSG)
        @Min(value = 1, message = INVALID_QUANTITY_ERROR_MSG)
        @Max(value = 100_000_000, message = INVALID_QUANTITY_ERROR_MSG)
        Integer quantity) {
}
