package gift.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import org.hibernate.validator.constraints.Length;

public record ProductRequest(Long id,
                             @NotBlank(message = "상품 이름이 옳지 않습니다.")
                             @Length(min = 1, max = 15, message = "상품 이름은 15자 이내 여야 합니다.")
                             @Pattern(regexp = "[a-zA-Z0-9가-힣\\(\\)\\[\\]\\-+&_\\/\\s]+", message = "상품 이름에는 (), [], -, +, &, _, /, 공백을 제외한 특수 문자를 사용할 수 없습니다.")
                             @Pattern(regexp = "^(?!.*카카오).*$", message = "상품 이름에 '카카오' 가 포함 되어 있습니다. 담당 MD와 협의가 필요합니다.")
                             String name,
                             int price,
                             @NotBlank
                             @Pattern(regexp = "^https://.*$", message = "이미지 주소가 적절하지 않습니다.")
                             String imageUrl,
                             @NotNull
                             Long categoryId
) {
}
