package gift.domain;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Pattern;

public record OptionRequest(
        Long id,
        @Pattern(regexp = "^[a-zA-Z0-9가-힣\\s\\(\\)\\[\\]\\+\\-\\&\\/\\_]*$", message = " ( ), [ ], +, -, &, /, _ 이외의 특수문자는 사용이 불가능합니다")
        String name,
        @Min(1)
        @Max(1000000000)
        Long quantity
) {
}
