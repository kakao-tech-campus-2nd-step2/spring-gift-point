package gift.domain.order.dto;

import gift.domain.order.entity.OrderItem;
import gift.domain.product.dto.OptionResponse;
import gift.domain.product.dto.ProductResponse;

public record OrderItemResponse(
    Long id,
    ProductResponse product,
    OptionResponse option,
    int quantity
) {
    public static OrderItemResponse from(OrderItem orderItem) {
        return new OrderItemResponse(
            orderItem.getId(),
            ProductResponse.from(orderItem.getProduct()),
            OptionResponse.from(orderItem.getOption()),
            orderItem.getQuantity()
        );
    }
}
