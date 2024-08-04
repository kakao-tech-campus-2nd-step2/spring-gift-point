package gift.event;

import gift.model.Orders;

public record OrderEventDto(
        Long productId,
        Long optionId,
        Long memberId,
        String productName,
        String productImageUrl,
        String optionName,
        int price,
        int quantity,
        String description,
        int point
) {
    public static OrderEventDto toDto(Orders order) {
        return new OrderEventDto(order.getProductId(), order.getOptionId(), order.getMemberId(),
                order.getProductName(), order.getProductImageUrl(), order.getOptionName(),
                order.getPrice(), order.getQuantity(), order.getDescription(), order.getPoint());
    }
}
