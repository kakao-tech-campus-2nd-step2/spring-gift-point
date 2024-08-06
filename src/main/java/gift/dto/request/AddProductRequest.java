package gift.dto.request;

import jakarta.validation.constraints.*;

import static gift.constant.ErrorMessage.*;

public record AddProductRequest(

        @NotBlank(message = REQUIRED_FIELD_MSG)
        @Size(max = 15, message = LENGTH_ERROR_MSG)
        @Pattern(regexp = "^[a-zA-Z0-9가-힣 ()\\[\\]+\\-&/_]*$", message = SPECIAL_CHAR_ERROR_MSG)
        @Pattern(regexp = "^(?!.*카카오).*$", message = KAKAO_CONTAIN_ERROR_MSG)
        String name,

        @NotNull(message = REQUIRED_FIELD_MSG)
        @Positive(message = POSITIVE_NUMBER_REQUIRED_MSG)
        Integer price,

        @NotBlank(message = REQUIRED_FIELD_MSG)
        String imageUrl,

        @NotBlank(message = REQUIRED_FIELD_MSG)
        String category,

        @NotBlank(message = REQUIRED_FIELD_MSG)
        @Size(max = 50, message = LENGTH_ERROR_MSG)
        @Pattern(regexp = "^[a-zA-Z0-9가-힣 ()\\[\\]+\\-&/_]*$", message = SPECIAL_CHAR_ERROR_MSG)
        String optionName,

        @NotNull(message = REQUIRED_FIELD_MSG)
        @Min(1) @Max(100_000_000)
        Integer optionQuantity) {
}
