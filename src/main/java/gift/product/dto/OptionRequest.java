package gift.product.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record OptionRequest(
        @NotEmpty(message = "반드시 값이 존재해야 합니다.")
        @Pattern(regexp = "[\\w\\d\\s\\(\\)\\[\\]\\+\\-\\&\\/\\_\\uAC00-\\uD7A3]*", message = "특수 문자를 제외한 문자열을 입력해야 합니다.")
        @Size(max = 50, message = "길이가 50 이하여야 합니다.")
        String name,
        @NotNull
        @Min(value = 1, message = "최소 1개 이상 입력해야 합니다.")
        @Max(value = 99_999_999, message = "최대 99,999,999개 이하 입력해야 합니다.")
        Integer quantity) { }
