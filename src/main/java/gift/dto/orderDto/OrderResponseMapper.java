package gift.dto.orderDto;

import gift.model.order.Order;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class OrderResponseMapper {
    public OrderResponseDto toOrderResponseDto(Order order){
        return new OrderResponseDto(order.getId(),order.getOption().getId(),order.getQuantity(), LocalDateTime.now(), order.getMessage());
    }
}
