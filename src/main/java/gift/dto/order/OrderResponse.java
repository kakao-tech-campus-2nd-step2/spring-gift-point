package gift.dto.order;

import com.fasterxml.jackson.annotation.JsonFormat;
import gift.model.order.Order;

import java.time.LocalDateTime;

public class OrderResponse {
    public record Info(
            Long id,
            Long productId,
            Long optionId,
            int quantity,
            LocalDateTime orderDateTime,
            String message

    ){
        public static Info fromEntity(Order order) {
            return new Info(order.getId(), order.getProduct().getId(), order.getOption().getId(), order.getQuantity(), order.getOrderDateTime(), order.getMessage());
        }

    }

    public record DetailInfo(
            Long orderId,
            Long productId,
            String productName,
            String productIamgeUrl,
            Long optionId,
            String optionName,
            int price,
            int totalPrice,
            int count,
            @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
            LocalDateTime createdAt
    ){
        public static DetailInfo fromEntity(Order order){
            int totalPrice = order.getProduct().getPrice() * order.getQuantity();
            return new DetailInfo(order.getId(),order.getProduct().getId(),order.getProduct().getName(),
                    order.getProduct().getImageUrl(),order.getOption().getId(),order.getOption().getName(),
                    order.getProduct().getPrice(),totalPrice,order.getQuantity(),order.getOrderDateTime());
        }

    }
}
