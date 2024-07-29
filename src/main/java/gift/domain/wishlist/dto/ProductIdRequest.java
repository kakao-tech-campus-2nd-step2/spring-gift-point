package gift.domain.wishlist.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "위시리시트에 담을 상품 요청 DTO")
public class ProductIdRequest {

    @Schema(description = "상품 ID", example = "1")
    private Long productId;

    private ProductIdRequest() {
    }

    public ProductIdRequest(Long productId) {
        this.productId = productId;
    }

    public Long getProductId() {
        return productId;
    }
}
