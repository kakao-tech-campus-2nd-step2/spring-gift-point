package gift.model;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

public record OptionDTO(
    long id,
    @Pattern(regexp = "^[A-Za-z가-힣0-9()\\[\\]\\-&/_+\\s]{1,50}$", message = "최대 공백 포함 50글자만 가능합니다. \n또한 특수 문자는 (, ), [, ], +, -, _, &, / 만 가능합니다.")
    String name,
    @NotNull(message = "수량은 필수 입력사항 입니다. 수량이 없다면 0을 입력하세요.") long quantity
) {

}
