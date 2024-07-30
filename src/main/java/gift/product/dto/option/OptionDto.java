package gift.product.dto.option;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record OptionDto(
    @Schema(example = "string")
    @Size(max = 50, message = "옵션 이름은 공백 포함 최대 50자까지 입력할 수 있습니다.")
    @Pattern(regexp = "^[a-zA-Z0-9가-힣 ()\\[\\]+\\-&/_]*$", message = "사용 가능한 특수 문자는 ()[]+-&/_ 입니다.")
    String name,
    @Min(value = 1, message = "옵션 수량은 최소 1개 이상 1억 개 미만이어야 합니다.")
    @Max(value = 99_999_999, message = "옵션 수량은 최소 1개 이상 1억 개 미만이어야 합니다.")
    int quantity
) {

}
