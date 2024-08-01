package gift.mapper;

import gift.domain.option.Option;
import gift.domain.order.Order;
import gift.web.dto.OrderDto;
import org.springframework.stereotype.Component;

@Component
public class OrderMapper {

    public OrderDto toDto(Order order) {
        return new OrderDto(
            order.getId(),
            order.getOption().getId(),
            order.getQuantity(),
            order.getOrderDateTime(),
            order.getMessage()
        );
    }

    public Order toEntity(OrderDto orderDto, Option option) {
        return new Order(option, orderDto.quantity(), orderDto.message());
    }
}
