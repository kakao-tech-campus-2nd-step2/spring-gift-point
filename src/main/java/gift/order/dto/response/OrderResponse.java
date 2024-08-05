package gift.order.dto.response;

import gift.order.entity.Order;

public record OrderResponse(
    Long orderId,
    Integer totalPrice,
    Integer discountedPrice,
    Integer accumulatedPoint
) {

    public static OrderResponse from(Order order) {
        return new OrderResponse(
            order.getId(),
            order.getTotalPrice(),
            order.getDiscountedPrice(),
            order.getAccumulatedPoint()
        );
    }

}
