package gift.controller.order.dto;

import gift.model.Option;
import gift.model.Order;
import gift.model.Product;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class OrderResponse {
    public record Info(
        Long orderId,
        Long productId,
        String productName,
        String productImageUrl,
        Long optionId,
        String optionName,
        int price,
        int totalPrice,
        int count,
        LocalDate createdAt
    ){
        public static Info from(Order order) {
            return new Info(
                order.getId(),
                order.getProduct().getId(),
                order.getProduct().getName(),
                order.getProduct().getImageUrl(),
                order.getOption().getId(),
                order.getOption().getName(),
                order.getProduct().getPrice(),
                order.getQuantity() * order.getProduct().getPrice(),
                order.getQuantity(),
                order.getCreatedAt().toLocalDate()
            );
        }

    }
}
