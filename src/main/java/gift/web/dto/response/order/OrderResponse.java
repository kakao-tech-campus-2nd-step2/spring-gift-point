package gift.web.dto.response.order;

import gift.domain.Order;
import java.time.LocalDateTime;

public class OrderResponse {

    private Long id; //주문 아이디

    private Long optionId;

    private Integer quantity;

    private String message;

    private LocalDateTime orderDateTime;

    public OrderResponse(Long id, Long optionId, Integer quantity, String message, LocalDateTime orderDateTime) {
        this.id = id;
        this.optionId = optionId;
        this.quantity = quantity;
        this.message = message;
        this.orderDateTime = orderDateTime;
    }

    public static OrderResponse from(Order order) {
        return new OrderResponse(order.getId(), order.getProductOptionId(), order.getQuantity(), order.getMessage(), order.getOrderDateTime());
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

    public String getMessage() {
        return message;
    }

    public LocalDateTime getOrderDateTime() {
        return orderDateTime;
    }
}