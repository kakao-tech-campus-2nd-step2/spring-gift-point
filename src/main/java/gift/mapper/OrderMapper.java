package gift.mapper;

import gift.domain.option.Option;
import gift.domain.order.Order;
import gift.web.dto.order.OrderRequestDto;
import gift.web.dto.order.OrderResponseDto;
import org.springframework.stereotype.Component;

@Component
public class OrderMapper {

    public OrderResponseDto toDto(Order order) {
        return new OrderResponseDto(
            order.getId(),
            order.getOption().getId(),
            order.getQuantity(),
            order.getOrderDateTime(),
            order.getMessage()
        );
    }

    public Order toEntity(OrderRequestDto orderRequestDto, Option option) {
        return new Order(option, orderRequestDto.quantity(), orderRequestDto.message());
    }
}
