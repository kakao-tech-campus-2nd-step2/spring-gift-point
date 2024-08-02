package gift.DTO;

import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.*;

/**
 * 제품의 세부 사항을 나타내는 클래스. ID, 이름, 가격, 이미지 URL을 포함한다.
 */
public record ProductOptionDTO(
        Long id,

        @NotNull(message = "이름은 null이 될 수 없습니다.")
        @Size(min = 1, max = 15, message = "이름은 1자에서 15자 사이여야 합니다.")
        @Pattern(regexp = "^[\\p{L}\\p{N}\\s\\(\\)\\[\\]\\+\\-\\&\\/\\_]*$", message = "이름은 문자, 숫자, 공백 및 특수 문자 ( ) [ ] + - & / _ 만 포함할 수 있습니다.")
        @Pattern(regexp = "^(?!.*카카오).*$", message = "카카오가 들어가는 품목명은 담당 MD와 협의한 경우에만 사용 가능합니다.")
        String name,

        @NotNull(message = "수량은 null이 될 수 없습니다.")
        @Min(value = 0, message = "수량은 0 이상이어야 합니다.")
        @Max(value = 100_000_000, message = "수량은 1억 이하여야 합니다.")
        long quantity,

        Long productId
) {}
