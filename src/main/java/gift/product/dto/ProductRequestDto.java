package gift.product.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

// 사용자가 입력하는 dto
public record ProductRequestDto(
    @Size(max = 15, message = "상품명은 최대 15자입니다.")
    @NotBlank(message = "상품명은 공백일 수 없습니다.")
    @Pattern(regexp = "^[\\(\\)\\[\\]\\+\\-\\&\\/\\_\\p{Alnum}\\s\\uAC00-\\uD7A3]+$", message = "상품명에 ( ), [ ], +, -, &, /, _를 제외한 특수문자를 사용할 수 없습니다.")
    @Pattern(regexp = "^(?!.*카카오).*$", message = "카카오라는 이름은 MD와 사전 협의 후 사용 가능합니다.")
    String name,

    @Min(value = 1, message = "가격은 0원보다 높아야 합니다.")
    @NotNull(message = "가격은 필수로 입력해야 합니다.")
    int price,

    @NotBlank(message = "잘못된 이미지입니다.")
    String imageUrl
) {

}
