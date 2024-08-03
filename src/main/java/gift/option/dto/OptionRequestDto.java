package gift.option.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record OptionRequestDto(
    @Pattern(regexp = "^[\\(\\)\\[\\]\\+\\-\\&\\/\\_\\p{Alnum}\\s\\uAC00-\\uD7A3]+$", message = "상품명에 ( ), [ ], +, -, &, /, _를 제외한 특수문자를 사용할 수 없습니다.")
    @Size(min = 1, max = 50)
    @NotBlank
    String name,

    @Min(value = 1, message = "수량을 입력할 때는 자연수를 입력해주세요.")
    @Max(value = 100_000_000, message = "옵션의 개수는 1억개를 넘을 수 없습니다.")
    int quantity
) {

}
