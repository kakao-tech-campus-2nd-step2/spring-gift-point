package gift.users.kakao;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

@Schema(description = "카카오 주문하기 DTO")
public record KakaoOrderDTO(
    @Schema(description = "주문할 상품 아이디", example = "1")
    @NotNull(message = "상품 아이디를 입력하지 않았습니다.")
    long productId,
    @Schema(description = "주문할 상품 옵션 아이디", example = "2")
    @NotNull(message = "옵션 아이디를 입력하지 않았습니다.")
    long optionId,
    @Schema(description = "주문할 상품의 수량", example = "1")
    @NotNull(message = "상품 수량을 입력하지 않았습니다.")
    @Min(value = 1, message = "상품은 한 개부터 구매 가능합니다.")
    int quantity,
    String orderDateTime,
    @Schema(description = "주문 시 같이 보낼 메시지", example = "조심해서 보내주세요")
    String message) {
}
