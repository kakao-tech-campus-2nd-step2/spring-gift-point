package gift.main.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import gift.main.entity.Order;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public record OrderResponse(
        long buyerId,
        long optionId,
        long productId,
        String buyerName,
        String productName,
        String optionName,
        int quantity,
        String message,
        int payPrice,
        int usingPoint,
        int totalPrice

) {
    //만드는 주체는 누가 가지는게 좋을까??
    public OrderResponse(Order order) {
        this(
                order.getBuyer().getId(),
                order.getOption().getId(),
                order.getProduct().getId(),
                order.getBuyer().getName(),
                order.getProduct().getName(),
                order.getOption().getOptionName(),
                order.getQuantity(),
                order.getMessage(),
                order.getPayPrice(),
                order.getUsingPoint(),
                order.getTotalPrice()
        );
    }

    @Override
    public String toString() {
        return "OrderResponse{" +
                "productName='" + productName + '\'' +
                ", optionName='" + optionName + '\'' +
                ", quantity=" + quantity +
                ", message='" + message + '\'' +
                '}';
    }
}
