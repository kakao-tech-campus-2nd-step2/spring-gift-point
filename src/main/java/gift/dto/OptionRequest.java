package gift.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import java.util.Objects;

public record OptionRequest(
        @NotBlank(message = "상품 이름은 공백일 수 없습니다.")
        @Size(max = 50, message = "상품 이름은 공백을 포함하여 최대 50자까지 입력할 수 있습니다.")
        @Pattern(regexp = "^[a-zA-Z0-9 ()\\[\\]+\\-&/_\\uAC00-\\uD7A3\\u3131-\\u3163]*$", message = "옵션 이름이 유효하지 않은 문자를 포함하고 있습니다.")
        String name,

        @Min(value = 1, message = "하나 이상의 옵션이 있어야 합니다.")
        int quantity,

        Long productId
) {
    public OptionRequest {
        Objects.requireNonNull(name, "상품 이름은 공백일 수 없습니다.");
    }
}
