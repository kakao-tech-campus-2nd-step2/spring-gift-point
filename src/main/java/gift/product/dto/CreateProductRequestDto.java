package gift.product.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record CreateProductRequestDto(
    @Size(max = 15, message = "상품명은 최대 15자입니다.")
    @NotBlank(message = "상품명은 공백일 수 없습니다.")
    @Pattern(regexp = "^[\\(\\)\\[\\]\\+\\-\\&\\/\\_\\p{Alnum}\\s\\uAC00-\\uD7A3]+$", message = "상품명에 ( ), [ ], +, -, &, /, _를 제외한 특수문자를 사용할 수 없습니다.")
    @Pattern(regexp = "^(?!.*카카오).*$", message = "카카오라는 이름은 MD와 사전 협의 후 사용 가능합니다.")
    String name,

    @Min(value = 1, message = "가격은 0원보다 높아야 합니다.")
    @NotNull(message = "가격은 필수로 입력해야 합니다.")
    int price,

    @NotBlank(message = "잘못된 이미지입니다.")
    String imageUrl,

    @NotBlank(message = "카테고리를 입력해주세요.")
    long categoryId,

    @Pattern(regexp = "^[\\(\\)\\[\\]\\+\\-\\&\\/\\_\\p{Alnum}\\s\\uAC00-\\uD7A3]+$", message = "옵션명에 ( ), [ ], +, -, &, /, _를 제외한 특수문자를 사용할 수 없습니다.")
    @Size(min = 1, max = 50)
    @NotBlank
    String optionName,

    @Min(value = 1, message = "수량을 입력할 때는 자연수를 입력해주세요.")
    @Max(value = 100_000_000, message = "옵션의 개수는 1억개를 넘을 수 없습니다.")
    int optionQuantity
) {

}
