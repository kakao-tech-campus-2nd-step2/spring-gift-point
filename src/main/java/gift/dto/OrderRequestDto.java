package gift.dto;

import gift.vo.Order;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

@Schema(description = "상품 주문 요청 DTO")
public record OrderRequestDto (

        @NotNull
        @Schema(description = "옵션 ID")
        Long optionId,
        
        @NotNull 
        @Schema(description = "주문 수량")
        int quantity,
        
        @Schema(description = "주문 메시지")
        String message

){
    public Order toOrder(Long memberId) {
        return new Order(memberId, optionId, quantity, LocalDateTime.now(), message);
    }
}
