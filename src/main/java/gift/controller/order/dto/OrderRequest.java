package gift.controller.order.dto;

import gift.model.Option;
import gift.model.Order;
import gift.model.Product;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record OrderRequest(

    @NotNull
    Long productId,
    @NotNull
    Long optionId,
    @Min(0)
    int quantity,
    @NotBlank
    String message,
    @Min(0)
    int point
    ) {

    public Order toEntity(Option option, Product product, Long userId) {
        return new Order(option, product, quantity, userId);
    }
}
