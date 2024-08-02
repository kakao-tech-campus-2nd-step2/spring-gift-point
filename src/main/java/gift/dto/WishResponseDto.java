package gift.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "위시 응답 DTO")
public class WishResponseDto {
    @Schema(description = "위시 고유 id")
    private Long id;
    @Schema(description = "위시리스트에 추가된 상품의 id")
    private Long productId;
    @Schema(description = "해당 위시 요청하는 사람의 id 값")
    private Long memberId;

    public WishResponseDto(Long id, Long productId,Long memberId) {
        this.id = id;
        this.productId = productId;
        this.memberId = memberId;
    }

    public WishResponseDto(Long productId, Long memberId) {
        this(null, productId,memberId);
    }

    public Long getId() {
        return id;
    }

    public Long getProductId() {
        return productId;
    }

    public Long getMemberId() {
        return memberId;
    }
}
