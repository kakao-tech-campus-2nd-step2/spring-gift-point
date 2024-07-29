package gift.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Pattern;
import org.hibernate.validator.constraints.Length;

public record OptionRequest(
        Long id,
        @Pattern(regexp = "[a-zA-Z0-9가-힣\\(\\)\\[\\]\\-+&_\\/\\s]+", message = "옵션 이름에는 (), [], -, +, &, _, /, 공백을 제외한 특수 문자를 사용할 수 없습니다.")
        @Length(max = 50, message = "옵션 이름에는 공백을 포함하여 최대 50자까지 입력할 수 있습니다.")
        String name,
        @Min(1)
        @Max(99999999)
        Long quantity
) {
}
