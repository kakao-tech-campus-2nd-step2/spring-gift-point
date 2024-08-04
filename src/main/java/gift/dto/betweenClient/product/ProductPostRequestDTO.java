package gift.dto.betweenClient.product;

import gift.entity.Category;
import gift.entity.Product;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.URL;

public record ProductPostRequestDTO(

        @NotBlank(message = "상품 이름을 입력해주세요.")
        String name,

        @NotNull(message = "가격을 입력해주세요")
        Integer price,

        @NotBlank(message = "이미지 URL을 입력해주세요.")
        @URL(message = "URL 형식이 아닙니다.")
        String imageUrl,

        @NotBlank(message = "카테고리 이름을 입력해주세요.")
        String categoryName,

        @NotBlank(message = "옵션 이름을 입력해주세요.")
        String optionName,

        @NotNull(message = "옵션 수량을 입력해주세요.")
        Integer optionQuantity
) {
    public Product convertToProduct(Category category) {
        return new Product(this.name, this.price, this.imageUrl, category);
    }

}
