package gift.controller.dto.response;

import gift.model.Orders;

import java.time.LocalDate;

public record OrderResponse(
        Long orderId,
        Long productId,
        String ProductName,
        String productImageUrl,
        Long optionId,
        String optionName,
        int price,
        int totalPrice,
        int count,
        LocalDate createdAt
) {
    public static OrderResponse from(Orders orders) {
        return new OrderResponse(
                orders.getId(),
                orders.getProductId(),
                orders.getProductName(),
                orders.getProductImageUrl(),
                orders.getOptionId(),
                orders.getOptionName(),
                orders.getPrice(),
                orders.getTotalPrice(),
                orders.getQuantity(),
                orders.getCreatedAt().toLocalDate()
        );
    }
}
