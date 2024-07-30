package gift.dto;

import gift.domain.Option;
import gift.domain.Product;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import org.hibernate.validator.constraints.Range;

public record OptionDTO(
    long id,

    @NotNull
    @Size(max = 50, message = "옵션 이름은 공백을 포함하여 최대 50자까지 입력할 수 있습니다.")
    @Pattern(regexp = "^[ㄱ-ㅎㅏ-ㅣ가-힣a-zA-Z0-9 ()\\[\\]+\\-&/_]*$", message = "한글, 영어, 숫자, 특수 문자 ( ), [ ], +, -, &, /, _ 만 가능합니다.")
    String name,

    @NotNull
    @Range(min = 1, max = 99999999)
    int quantity
) {

    public Option toEntity(Product product) {
        return new Option(name, quantity, product);
    }
}
