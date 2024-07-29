package gift.dto.wish;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "위시리스트 항목 응답 데이터")
public record WishResponse(
    @Schema(description = "위시리스트 항목 ID", example = "1")
    Long id,

    @Schema(description = "회원 ID", example = "1")
    Long memberId,

    @Schema(description = "상품 ID", example = "1")
    Long productId
) {

}
