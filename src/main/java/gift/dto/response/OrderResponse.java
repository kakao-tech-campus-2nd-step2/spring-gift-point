package gift.dto.response;

import gift.domain.Order;

import java.time.LocalDateTime;

public class OrderResponse {

    private Long id;
    private Long optionId;
    private Integer quantity;
    private LocalDateTime orderDateTime;
    private String message;
    private Long receiveMemberId;

    public OrderResponse(Long id, Long optionId, Integer quantity, LocalDateTime orderDateTime, String message, Long receiveMemberId) {
        this.id = id;
        this.optionId = optionId;
        this.quantity = quantity;
        this.orderDateTime = orderDateTime;
        this.message = message;
        this.receiveMemberId = receiveMemberId;
    }

    public static OrderResponse fromOrder(Order order) {
        return new OrderResponse(
                order.getId(),
                order.getOptionId(),
                order.getQuantity(),
                order.getOrderDateTime(),
                order.getMessage(),
                order.getReceiveMemberId()
        );
    }

    public Long getOptionId() {
        return optionId;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public String getMessage() {
        return message;
    }

    public Long getReceiveMemberId() {
        return receiveMemberId;
    }
}
