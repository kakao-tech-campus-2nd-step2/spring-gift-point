package gift.product.dto;

import gift.option.dto.OptionReqDto;
import gift.product.entity.Product;
import gift.product.message.ProductInfo;
import gift.product.validation.ValidProductName;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.util.List;

public record ProductReqDto(

        // 상품의 이름은 공백을 포함하여 최대 15자까지 입력 가능
        // 특수 문자는 (), [], +, -, &, /, _ 만 허용
        // "카카오" 문구가 들어간 경우에는 담당 MD와 협의한 경우에만 사용 가능
        @ValidProductName
        String name,

        @NotNull(message = ProductInfo.PRODUCT_PRICE_REQUIRED)
        Integer price,

        @NotBlank(message = ProductInfo.PRODUCT_IMAGE_URL_REQUIRED)
        String imageUrl,

        @NotBlank(message = ProductInfo.PRODUCT_CATEGORY_REQUIRED)
        String category,

        @Size(min = 1, message = "상품 옵션은 최소 1개 이상 입력해야 합니다.")
        List<@Valid OptionReqDto> options
) {

    public Product toEntity() {

        return new Product(
                name,
                price,
                imageUrl,
                null
        );
    }
}
