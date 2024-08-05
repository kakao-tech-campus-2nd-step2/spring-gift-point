package gift.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Response DTO for WishPageContent")
public record WishResponseForPage(
    @Schema(description = "Wish ID", example = "1")
    Long wishId,
    @Schema(description = "Product ID", example = "1")
    Long productId,
    @Schema(description = "Product Name", example = "이마트 모바일 금액권")
    String productName,
    @Schema(description = "Product Price", example = "50000")
    int productPrice,
    @Schema(description = "Product ImageUrl", example = "https://img1.kakaocdn.net/thumb/C320x320@2x.fwebp.q82/?fname=https%3A%2F%2Fst.kakaocdn.net%2Fproduct%2Fgift%2Fproduct%2F20240319133340_790b6fb28fa24f1fbb3eee4471c0a7fb.jpg")
    String productImageUrl
) {

}
