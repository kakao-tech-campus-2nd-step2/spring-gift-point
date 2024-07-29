package gift.order.util;

import gift.member.entity.Member;
import gift.order.dto.OrderRequest;
import gift.order.dto.OrderResponse;
import gift.order.entity.Order;
import gift.product.entity.Option;

public class OrderMapper {

    public static Order toEntity(OrderRequest request, Option option, Member member) {
        return new Order(
                request.message(),
                option,
                member
        );
    }

    public static OrderResponse toResponseDto(Order order) {
        return new OrderResponse(
                order.getId(),
                order.getOptionId(),
                order.getQuantity(),
                order.getOrderDateTime(),
                order.getMessage()
        );
    }

}
