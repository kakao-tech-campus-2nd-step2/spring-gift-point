package gift.dto;

import gift.model.Order;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;

@Schema(description = "주문 DTO")
public class OrderDTO {

    @Schema(description = "주문 ID", example = "1")
    private Long id;

    @Schema(description = "옵션 ID", example = "1")
    private Long optionId;

    @Schema(description = "주문 수량", example = "2")
    private int quantity;

    @Schema(description = "주문 메시지", example = "빠른 배송 부탁드립니다.")
    private String message;

    @Schema(description = "주문 시간", example = "2024-07-26T12:34:56")
    private LocalDateTime orderDateTime;

    @Schema(description = "사용 후 남은 포인트", example = "950")
    private int newPoints;

    public OrderDTO() {
    }

    public OrderDTO(Long id, Long optionId, int quantity, String message, LocalDateTime orderDateTime) {
        this.id = id;
        this.optionId = optionId;
        this.quantity = quantity;
        this.message = message;
        this.orderDateTime = orderDateTime;
    }

    public static OrderDTO from(Order order) {
        return new OrderDTO(
            order.getId(),
            order.getOption().getId(),
            order.getQuantity(),
            order.getMessage(),
            order.getOrderDateTime()
        );
    }

    public Long getId() {
        return id;
    }

    public Long getOptionId() {
        return optionId;
    }

    public int getQuantity() {
        return quantity;
    }

    public String getMessage() {
        return message;
    }

    public LocalDateTime getOrderDateTime() {
        return orderDateTime;
    }

    public int getNewPoints() {
        return newPoints;
    }

    public void setNewPoints(int newPoints) {
        this.newPoints = newPoints;
    }
}
