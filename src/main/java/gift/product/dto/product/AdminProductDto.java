package gift.product.dto.product;

import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record AdminProductDto(
    @Size(max = 15, message = "상품 이름은 공백 포함 최대 15자까지 입력할 수 있습니다.")
    @Pattern(regexp = "^[a-zA-Z0-9가-힣 ()\\[\\]+\\-&/_]*$", message = "사용 가능한 특수 문자는 ()[]+-&/_ 입니다.")
    String name,
    int price,
    String imageUrl,
    String categoryName) implements ProductDto {

}
