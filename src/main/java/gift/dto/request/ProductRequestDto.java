package gift.dto.request;

import gift.domain.Product;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;

public record ProductRequestDto(
        @Pattern(
                regexp = "^[a-zA-Z0-9가-힣 ()\\[\\]+\\-&/_]{1,15}$",
                message = "상품 이름은 15자 이내여야 하며, 알파벳, 숫자, 한글, 공백 및 허용된 특수 문자만 포함할 수 있습니다."
        )
        String name,

        @NotNull(message = "가격을 입력하세요 (1 이상)")
        @Positive(message = "가격은 1 이상이어야 합니다")
        Integer price,
        String imageUrl,

        @NotNull(message = "카테고리를 등록하세요")
        Long categoryId) {

    public static ProductRequestDto of(String name, int price, String imageUrl, Long categoryId) {
        return new ProductRequestDto(name, price, imageUrl, categoryId);
    }

    public static ProductRequestDto from(Product product) {
        return new ProductRequestDto(product.getName(), product.getPrice(), product.getImageUrl(), product.getCategory().getId());
    }

}
