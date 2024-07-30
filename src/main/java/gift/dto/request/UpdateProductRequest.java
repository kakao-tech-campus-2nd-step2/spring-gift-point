package gift.dto.request;

import gift.validation.ContainsOnlyAllowedSpecialCharacter;
import gift.validation.NotContainsKakao;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record UpdateProductRequest(
        @NotNull
        Long id,
        @NotBlank
        @Size(max = 15, message = "상품 이름은 공백을 포함하여 최대 15자까지 입력할 수 있습니다.")
        @NotContainsKakao(message = "상품 이름에 `카카오`가 포함된 문구는 담당 MD와 협의한 경우에만 사용 가능합니다.")
        @ContainsOnlyAllowedSpecialCharacter(message = "상품 이름에 (, ), [, ], +, -, &, /, _ 와 같은 특수문자만 허용됩니다.")
        String name,
        @NotNull
        int price,
        @NotBlank
        String imageUrl,
        @NotNull
        Long categoryId
) {
}
