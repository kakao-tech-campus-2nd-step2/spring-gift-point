package gift.dto.product;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.PositiveOrZero;
import org.hibernate.validator.constraints.Length;

public record ProductRequest(
        @Pattern(regexp = "^[\s\\-\\&\\(\\)\\[\\]\\+\\/\\_a-zA-z0-9ㄱ-ㅎ가-힣]*$", message = "허용되지 않은 형식의 이름입니다.")
        @Length(max = 15, message = "이름의 길이는 15자를 초과할 수 없습니다.")
        @NotBlank(message = "이름의 길이는 최소 1자 이상이어야 합니다.")
        String name,
        @PositiveOrZero(message = "금액은 0보다 크거나 같아야 합니다.")
        Integer price,
        @NotBlank(message = "상품 이미지는 필수로 입력해야 합니다.")
        String imageUrl,
        @NotNull(message = "상품 카테고리는 반드시 선택되어야 합니다.")
        Long categoryId
) {
}
