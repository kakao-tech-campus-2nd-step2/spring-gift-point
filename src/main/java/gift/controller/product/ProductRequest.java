package gift.controller.product;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;

public record ProductRequest(
    @NotBlank(message = "상품명은 필수 입력 항목입니다.")
    @Size(max = 15, message = "상품명은 최대 15자 이내입니다.")
    @Pattern(regexp = "^[\\w\\s\\[\\]()+\\-&\\/가-힣]*$", message = "적절하지 않은 문자가 포함되어 있습니다.")
    @Pattern(regexp = "^(?!.*카카오).*", message = "'카카오'가 포함된 문구는 담당 MD와 협의한 경우에만 사용 가능합니다.")
    String name,

    @PositiveOrZero Long price,

    String imageUrl,

    String categoryName) {

}