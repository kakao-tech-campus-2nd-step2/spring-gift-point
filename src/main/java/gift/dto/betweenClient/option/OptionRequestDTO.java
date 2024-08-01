package gift.dto.betweenClient.option;

import gift.entity.Option;
import gift.entity.Product;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import org.hibernate.validator.constraints.Length;

public record OptionRequestDTO(
        @NotBlank(message = "옵션 이름을 입력해주세요.")
        @Pattern(regexp = "[a-zA-Z0-9ㄱ-ㅎ가-힣()\\[\\]+\\-&/_ ]+", message = "( ), [ ], +, -, &, /, _을 제외한 특수문자는 입력할 수 없습니다.")
        @Length(min = 1, max = 50, message = "옵션 이름 길이는 1~50자만 가능합니다.")
        String name,

        @NotNull(message = "옵션 수량을 입력해주세요.")
        @Min(value = 1, message = "옵션 수량은 1개 이상, 1억개 미만만 가능합니다.")
        @Max(value = 99999999, message = "옵션 수량은 1개 이상, 1억개 미만만 가능합니다.")
        Integer quantity
) {
    public Option convertToOption(Product product){
        return new Option(product, name, quantity);
    }
}
