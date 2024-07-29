package gift.dto.option;


import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "옵션 응답 데이터")
public record OptionResponse(
    @Schema(description = "옵션 ID", example = "1")
    Long id,

    @Schema(description = "옵션 이름", example = "색상: 빨간색")
    String name,

    @Schema(description = "옵션 수량", example = "100")
    int quantity,

    @Schema(description = "상품 ID", example = "1")
    Long productId
) {

}
