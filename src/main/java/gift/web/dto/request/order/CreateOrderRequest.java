package gift.web.dto.request.order;

import gift.domain.Order;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public class CreateOrderRequest {

    @NotNull
    private final Long optionId;

    @Min(1)
    private final Integer quantity;

    @NotEmpty
    private final String message;

    public CreateOrderRequest(Long optionId, Integer quantity, String message) {
        this.optionId = optionId;
        this.quantity = quantity;
        this.message = message;
    }

    public Order toEntity(Long memberId, Long productId) {
        return new Order.Builder()
            .memberId(memberId)
            .productId(productId)
            .productOptionId(optionId)
            .quantity(quantity)
            .message(message)
            .build();
    }

    public Long getOptionId() {
        return optionId;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public String getMessage() {
        return message;
    }
}
