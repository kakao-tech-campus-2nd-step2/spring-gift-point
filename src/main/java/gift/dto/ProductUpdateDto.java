package gift.dto;

import gift.validation.ValidName;
import gift.vo.Category;
import gift.vo.Product;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Positive;

@Schema(description = "상품 업데이트 DTO")
public record ProductUpdateDto (

        @Schema(description = "상품 ID")
        Long id,

        @Schema(description = "카테고리 ID")
        Long categoryId,

        @ValidName
        @NotEmpty(message = "상품명을 입력해 주세요.")
        @Schema(description = "상품명")
        String name,

        @Positive(message = "가격을 입력해 주세요(0보다 커야 합니다.)")
        @Schema(description = "상품 가격")
        int price,

        @Schema(description = "상품 이미지 URL")
        String imageUrl
) {
    public Product toProduct(Category category) {
        return new Product(category, name, price, imageUrl);
    }
}
