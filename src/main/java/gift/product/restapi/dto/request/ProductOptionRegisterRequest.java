package gift.product.restapi.dto.request;

import gift.core.domain.product.ProductOption;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

public record ProductOptionRegisterRequest(
        @Size(min = 1, max = 50, message = "옵션명은 1자 이상 50자 이하여야 합니다.")
        @Pattern(regexp = "^[a-zA-Z0-9가-힣()\\[\\]+\\-&/_ ]*$", message = "특수문자는 ()[]+-&/_ 만 허용됩니다.")
        String name,

        @Positive(message = "수량은 1 이상이어야 합니다.")
        Integer quantity
) {
    public ProductOption toDomain() {
        return ProductOption.of(name, quantity);
    }
}
