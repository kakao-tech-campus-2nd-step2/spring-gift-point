package gift.dto.betweenClient.product;

import gift.entity.Category;
import gift.entity.Product;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.URL;

public record ProductRequestDTO(
        @NotBlank(message = "상품 이름을 입력해주세요.")
        String name,

        @NotNull(message = "가격을 입력해주세요")
        Integer price,

        @NotBlank(message = "이미지 URL을 입력해주세요.")
        @URL(message = "URL 형식이 아닙니다.")
        String imageUrl,

        @NotBlank(message = "카테고리 이름을 입력해주세요.")
        String categoryName
) {
    public Product convertToProduct(Category category) {
        return new Product(this.name, this.price, this.imageUrl, category);
    }

}
