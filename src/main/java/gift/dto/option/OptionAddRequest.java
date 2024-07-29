package gift.dto.option;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import org.hibernate.validator.constraints.Length;

public record OptionAddRequest(
        @Pattern(regexp = "^[\s\\-\\&\\(\\)\\[\\]\\+\\/\\_a-zA-z0-9ㄱ-ㅎ가-힣]*$", message = "허용되지 않은 형식의 이름입니다.")
        @Length(max = 50, message = "이름의 길이는 50자를 초과할 수 없습니다.")
        @NotBlank(message = "이름의 길이는 최소 1자 이상이어야 합니다.")
        String name,
        @Min(value = 1, message = "수량은 최소 1개 이상, 1억개 미만입니다.")
        @Max(value = 100_000_000, message = "수량은 최소 1개 이상, 1억개 미만입니다.")
        Integer quantity,
        @NotNull(message = "상품은 반드시 선택되어야 합니다.")
        Long productId
) {
}
