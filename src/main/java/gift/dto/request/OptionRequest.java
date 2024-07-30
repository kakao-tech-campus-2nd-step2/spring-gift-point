package gift.dto.request;

import gift.validation.ContainsOnlyAllowedSpecialCharacter;
import jakarta.validation.constraints.*;

public record OptionRequest(
        @NotBlank(message = "옵션 이름을 입력해주세요.")
        @Size(max = 50, message = "옵션 이름은 공백을 포함하여 최대 50자까지 입력할 수 있습니다.")
        @ContainsOnlyAllowedSpecialCharacter(message = "옵션 이름에 (, ), [, ], +, -, &, /, _ 와 같은 특수문자만 허용됩니다.")
        String name,
        @NotNull(message = "옵션 수량을 입력해주세요.")
        @Min(value = 1, message = "옵션 수량은 최소 1개 이상이어야 합니다.")
        @Max(value = 99999999, message = "옵션 수량은 1억 개 미만이어야 합니다.")
        Integer quantity
) {
}
