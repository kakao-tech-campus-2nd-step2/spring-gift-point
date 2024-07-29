package gift.domain;

import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record MenuRequest(
        @Size(max = 15, message = "메뉴명은 15자 이내로 해주세요")
        @Pattern(regexp = "^[a-zA-Z0-9가-힣\\s\\(\\)\\[\\]\\+\\-\\&\\/\\_]*$", message = " ( ), [ ], +, -, &, /, _ 이외의 특수문자는 사용이 불가능합니다")
        @Pattern(regexp = "^((?!카카오).)*$", message = "'카카오'가 포함된 상품명은 담당 MD와 협의해주세요")
        String name,
        int price,
        String imageUrl,
        Long categoryId
) {

}
