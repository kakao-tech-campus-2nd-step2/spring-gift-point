package gift.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import jdk.jfr.Description;

@Description("상품 추가하거나, 수정하는 경우 요청 DTO")
public record ProductRequestDTO(
        Long categoryId,
        @Size(max = 15, message = "상품 이름은 최대 15자까지 입력할 수 있습니다.")
        @Pattern(regexp = "^(?!.*카카오)[a-zA-Z0-9가-힣 ()\\[\\]+\\-&/_]*$",
                message = "상품 이름에 허용되지 않는 특수 문자가 포함되어 있습니다.")
        String name,
        @Min(value = 1, message = "가격은 1 이상이어야 합니다.")
        int price,
        String imageUrl
) {
}