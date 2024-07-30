package gift.domain.product.dto;

import gift.domain.product.entity.Option;
import gift.domain.product.entity.Product;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import org.hibernate.validator.constraints.Range;

@Schema(description = "상품 옵션 요청 정보")
public record OptionRequest(

    @NotBlank(message = "옵션 이름은 필수 입력 필드이며 공백으로만 구성될 수 없습니다.")
    @Size(max = 50, message = "옵션 이름은 50자를 초과할 수 없습니다.")
    @Pattern(regexp = "[a-zA-z0-9ㄱ-ㅎㅏ-ㅣ가-힣()\\[\\]+\\-&/_\\s]+", message = "(,),[,],+,-,&,/,_ 외의 특수 문자는 사용이 불가능합니다.")
    @Schema(description = "옵션 이름")
    String name,

    @Range(min = 1, max = 100000000, message = "옵션 수량은 1 이상 100,000,000 이하여야 합니다.")
    @Schema(description = "옵션 수량")
    int quantity
) {
    public Option toOption(Product product) {
        return new Option(null, product, name, quantity);
    }
}
