package gift.dto;

import gift.validation.ValidName;
import gift.vo.Category;
import gift.vo.Product;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Positive;

public record ProductUpdateDto (
        Long id,

        Long categoryId,

        @ValidName
        @NotEmpty(message = "상품명을 입력해 주세요.")
        String name,

        @Positive(message = "가격을 입력해 주세요(0보다 커야 합니다.)")
        int price,

        String imageUrl
) {
    public Product toProduct(Category category) {
        return new Product(category, name, price, imageUrl);
    }
}
