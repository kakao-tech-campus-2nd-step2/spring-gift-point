package gift.dto;

import gift.domain.Order;
import java.time.LocalDateTime;

public record OrderPagedResponseDto(String name,
                                    Integer price,
                                    Integer quantity,
                                    String imageUrl,
                                    LocalDateTime orderDateTime
) {

    public static OrderPagedResponseDto convertToDto(Order order, String name, Integer price, String imageUrl) {
        return new OrderPagedResponseDto(
            name,
            price,
            order.getQuantity(),
            imageUrl,
            order.getOrderDateTime()
        );
    }
}
