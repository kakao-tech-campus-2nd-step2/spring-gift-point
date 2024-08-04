package gift.service.dto;

import gift.event.OrderEventDto;

public record OrderDto(
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
    public static OrderDto from(OrderEventDto order) {
        return new OrderDto(order.productId(), order.optionId(), order.memberId(),
                order.productName(), order.productImageUrl(), order.optionName(),
                order.price(), order.quantity(), order.description(), order.point());
    }
}
