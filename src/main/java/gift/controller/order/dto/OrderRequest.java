package gift.controller.order.dto;

import gift.model.Option;
import gift.model.Order;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record OrderRequest(
    @NotNull
    Long optionId,
    @Min(0)
    int quantity,
    @NotBlank
    String message
    ) {

    public Order toEntity(Option option) {
        return new Order(option, quantity, message);
    }
}
