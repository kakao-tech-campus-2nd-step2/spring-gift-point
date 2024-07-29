package gift.controller.option;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record OptionRequest(
    @NotBlank(message = "상품명은 필수 입력 항목입니다.")
    @Size(max = 50, message = "상품명은 최대 15자 이내입니다.")
    @Pattern(regexp = "^[\\w\\s\\[\\]()+\\-&\\/가-힣]*$", message = "적절하지 않은 문자가 포함되어 있습니다.")
    String name,
    @Min(1) @Max(100_000_000) Integer quantity) {
}