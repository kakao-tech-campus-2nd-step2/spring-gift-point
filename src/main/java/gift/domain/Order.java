package gift.domain;

import gift.entity.OrderEntity;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import java.time.LocalDateTime;

public class Order {

    public record OrderRequest(
        @Min(1)
        Long optionId,
        @Min(1)
        @Max(99999999)
        Long quantity,
        @NotBlank
        String message
    ) {

    }

    public record OrderResponse(
        Long id,
        Long optionId,
        Long quantity,
        String message,
        LocalDateTime orderDateTime
    ) {
        public static OrderResponse from(OrderEntity orderEntity) {
            return new OrderResponse(
                orderEntity.getId(),
                orderEntity.getOptionEntity().getId(),
                orderEntity.getQuantity(),
                orderEntity.getMessage(),
                orderEntity.getOrderDateTime()
            );
        }
    }

}
