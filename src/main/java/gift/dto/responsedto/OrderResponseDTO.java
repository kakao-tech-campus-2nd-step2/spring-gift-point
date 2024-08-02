package gift.dto.responsedto;

import gift.domain.Order;
import java.time.LocalDateTime;

public record OrderResponseDTO(
    Long id,
    OptionResponseDTO optionResponseDTO,
    LocalDateTime orderDateTime,
    String message
) {
    public static OrderResponseDTO from(Order order) {
        return new OrderResponseDTO(order.getId(), OptionResponseDTO.from(order.getOption()),
            order.getOrderDateTime(), order.getMessage());
    }
}
