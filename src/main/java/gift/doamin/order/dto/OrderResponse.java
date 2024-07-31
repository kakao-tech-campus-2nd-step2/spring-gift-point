package gift.doamin.order.dto;

import gift.doamin.order.entity.Order;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;

@Schema(description = "주문 정보")
public class OrderResponse {

    @Schema(description = "주문 id")
    Long id;

    @Schema(description = "주문한 상품 옵션 id")
    Long optionId;

    @Schema(description = "주문한 수량")
    Integer quantity;

    @Schema(description = "주문 일시")
    LocalDateTime orderDateTime;

    @Schema(description = "수령인에게 보낸 메시지")
    String message;

    public OrderResponse(Order order) {
        this.id = order.getId();
        this.optionId = order.getOption().getId();
        this.quantity = order.getQuantity();
        this.orderDateTime = order.getOrderDateTime();
        this.message = order.getMessage();
    }

    public Long getId() {
        return id;
    }

    public Long getOptionId() {
        return optionId;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public LocalDateTime getOrderDateTime() {
        return orderDateTime;
    }

    public String getMessage() {
        return message;
    }
}
