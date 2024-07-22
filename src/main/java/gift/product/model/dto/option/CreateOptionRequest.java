package gift.product.model.dto.option;

import static gift.util.Utils.NAME_PATTERN;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record CreateOptionRequest(
        @NotBlank
        @Size(max = 15, message = "옵션 이름은 50자 이하로 입력해주세요.")
        @Pattern(regexp = NAME_PATTERN, message = "사용할 수 없는 특수문자가 포함되어 있습니다.")
        String name,

        @NotNull
        @Min(value = 1, message = "수량은 1 이상의 정수로 입력해주세요.")
        @Max(value = 100000000 - 1, message = "수량은 1억 이하의 정수로 입력해주세요.")
        int quantity,

        @Min(value = 0, message = "추가 금액은 0 이상의 정수로 입력해주세요.")
        @NotNull
        int additionalCost
) {
}
