package gift.product.dto.product;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record ClientProductDto(
    @Schema(description = "'카카오'가 포함된 문구는 담당 MD와 협의한 경우에만 사용할 수 있습니다.", pattern = "사용 가능한 특수 문자는 ()[]+-&/_ 입니다.", example = "string")
    @Size(max = 15, message = "상품 이름은 공백 포함 최대 15자까지 입력할 수 있습니다.")
    @Pattern(regexp = "^[a-zA-Z0-9가-힣 ()\\[\\]+\\-&/_]*$", message = "사용 가능한 특수 문자는 ()[]+-&/_ 입니다.")
    @Pattern(regexp = "^(?!.*카카오).*$", message = "'카카오'가 포함된 문구는 담당 MD와 협의한 경우에만 사용할 수 있습니다.")
    String name,
    int price,
    String imageUrl,
    String categoryName) implements ProductDto {

}
