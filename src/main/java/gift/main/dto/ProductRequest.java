package gift.main.dto;

import gift.main.annotation.IsValidName;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PositiveOrZero;

public record ProductRequest(
        @IsValidName String name,
        @PositiveOrZero(message = "상품 가격은 음수일 수 없습니다.") int price,
        @NotBlank(message = "이미지주소를 등록해주세요.") String imageUrl,
        int categoryId) {

    public ProductRequest(ProductAllRequest productAllRequest) {
        this(productAllRequest.name(), productAllRequest.price(), productAllRequest.imageUrl(), productAllRequest.categoryId());
    }
}
