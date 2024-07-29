package gift.dto;

import gift.vo.Order;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public record OrderRequestDto (

        @NotNull Long optionId,
        @NotNull int quantity,
        String message

){
    public Order toOrder(Long memberId) {
        return new Order(memberId, optionId, quantity, LocalDateTime.now(), message);
    }
}
