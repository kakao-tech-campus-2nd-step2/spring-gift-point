package gift.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record OptionDTO(
    @NotBlank(message = "옵션 이름은 최소 1자 이상이어야 합니다.")
    @Size(max = 50, message = "옵션 이름은 공백 포함 최대 50자까지 입력할 수 있습니다.")
    @Pattern(regexp = "^[a-zA-Z0-9가-힣ㄱ-ㅎㅏ-ㅣ\\s\\(\\)\\[\\]\\+\\-\\&\\/\\_]*$", message = "옵션 이름에 (), [], +, -, &, /, _ 외 특수 문자는 사용할 수 없습니다.")
    String name,

    @NotNull(message = "옵션 수량을 입력해야 합니다.")
    @Min(value = 1, message = "옵션 수량은 최소 1개 이상이어야 합니다.")
    @Max(value = 99_999_999, message = "옵션 수량은 최대 1억 개 미만까지 가능합니다.")
    Long quantity,

    @NotNull(message = "상품을 선택해야 합니다.")
    Long productId
) {

}
