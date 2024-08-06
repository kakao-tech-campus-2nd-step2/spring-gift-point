package gift.domain.product;

import gift.domain.category.Category;
import gift.domain.option.OptionRequest;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import java.util.List;

public record ProductRequest(
    @NotBlank(message = "상품명이 입력되지 않았습니다")
    @Size(max = 15, message = "상품명의 최대 길이는 15자입니다")
    @Pattern(regexp = "^[\\w\\s가-힣\\(\\)\\[\\]\\+\\-\\&\\/\\_]*$", message = "상품명에 특수 문자는 (, ), [, ], +, -, &, /, _ 만 사용할 수 있습니다")
    @Pattern(regexp = "^(?!.*카카오).*$", message = "상품명에 '카카오'를 포함하려면 담당 MD와 협의가 필요합니다")
    String name,

    @Min(value = 0, message = "상품 가격은 0 보다 작을 수 없습니다")
    Long price,

    @NotBlank(message = "상품 이미지가 입력되지 않았습니다")
    String imageUrl,

    @NotNull
    @Min(value = 0, message = "카테고리 번호가 올바르지 않습니다")
    Long categoryId,

    @NotNull
    List<OptionRequest> options
) {

    public Product toProduct() {
        return new Product(name, price, imageUrl, new Category(categoryId));
    }
}
