package gift.dto.product;

import gift.domain.Category;
import gift.domain.Product;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

public record ProductDto(
    long id,

    @NotNull
    @Size(max = 15, message = "상품 이름은 공백을 포함하여 최대 15자까지 입력할 수 있습니다.")
    @Pattern(regexp = "^[ㄱ-ㅎㅏ-ㅣ가-힣a-zA-Z0-9 ()\\[\\]+\\-&/_]*$", message = "한글, 영어, 숫자, 특수 문자 ( ), [ ], +, -, &, /, _ 만 가능합니다.")
    @Pattern(regexp = "^(?!.*카카오).*$", message = "'카카오'가 포함된 문구는 담당 MD와 협의한 경우에만 사용할 수 있습니다.")
    String name,

    @Positive(message = "상품 가격은 양수이어야 합니다.")
    int price,

    @NotNull
    String imageUrl,

    @NotNull
    long categoryId
) {

    public Product toEntity(Category category) {
        return new Product(name, price, imageUrl, category);
    }
}