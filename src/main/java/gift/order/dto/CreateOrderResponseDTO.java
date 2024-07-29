package gift.order.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;

@Schema(description = "주문 송신 DTO")
public class CreateOrderResponseDTO {

    @Schema(description = "구매한 상품의 id")
    long id;

    @Schema(description = "구매한 옵션의 id")
    long optionId;

    @Schema(description = "구매한 수량")
    int quantity;

    @Schema(description = "구매 시각")
    LocalDateTime orderDateTime;

    @Schema(description = "보낸 메시지")
    String message;

    public CreateOrderResponseDTO(long id, long optionId, int quantity, LocalDateTime orderDateTime,
        String message) {
        this.id = id;
        this.optionId = optionId;
        this.quantity = quantity;
        this.orderDateTime = orderDateTime;
        this.message = message;
    }

    public long getId() {
        return id;
    }

    public long getOptionId() {
        return optionId;
    }

    public int getQuantity() {
        return quantity;
    }

    public LocalDateTime getOrderDateTime() {
        return orderDateTime;
    }

    public String getMessage() {
        return message;
    }
}
