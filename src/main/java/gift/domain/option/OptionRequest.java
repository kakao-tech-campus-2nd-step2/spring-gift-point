package gift.domain.option;

import gift.domain.product.Product;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record OptionRequest(
    @NotBlank
    @Size(max = 50, message = "옵션명은 최대 50자까지만 입력할 수 있습니다")
    @Pattern(regexp = "^[\\w\\s가-힣\\.\\,\\(\\)\\[\\]\\+\\-\\&\\/\\_]*$", message = "옵션명에 허용되지 않은 문자가 포함되어 있습니다")
    String name,

    @NotNull
    @Min(value = 0, message = "수량은 최소 1개 이상이여야 합니다")
    @Max(value = 100000000, message = "수량은 1억 미만이어야 합니다")
    Long quantity
) {

    public Option toOption(Long productId) {
        return new Option(name, quantity, new Product(productId));
    }
}
