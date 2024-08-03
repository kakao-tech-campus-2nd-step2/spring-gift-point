package gift.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "위시 요청 DTO")
public class WishRequestDto {
    @Schema(description = "위시 고유 id")
    private Long id;
    @Schema(description = "위시리스트에 추가된 상품의 id")
    private Long productId;

    public WishRequestDto(Long productId) {
        this.productId = productId;
    }

    public Long getId() {
        return id;
    }

    public Long getProductId() {
        return productId;
    }


}

