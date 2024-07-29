package gift.dto.request;

import gift.domain.Option;
import gift.domain.Product;
import jakarta.validation.constraints.*;

public record OptionRequest(
        @NotBlank(message = "옵션 이름은 공백일 수 없습니다.")
        @Size(max = 50, message = "옵션 이름은 공백을 포함하여 최대 50자까지 입력할 수 있습니다.")
        @Pattern(regexp = "^[a-zA-Z0-9가-힣 ()\\[\\]+\\-&/_]*$", message = "옵션 이름에 허용되지 않은 특수 문자가 포함되어 있습니다.")
        String name,

        @Min(value = 1, message = "옵션 수량은 1 이상이어야 합니다.")
        @Max(value = 99999999, message = "옵션 수량은 1억 미만이어야 합니다.")
        int quantity) {

    public Option toEntity(Product product) {
        return new Option(this.name, this.quantity, product);
    }
}
