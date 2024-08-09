package gift.web.dto.response.order;

import gift.domain.Order;
import gift.domain.vo.Point;
import java.time.LocalDateTime;

public class CreateOrderResponse {

    private Long id; //주문 아이디

    private Long optionId;

    private Integer quantity;

    private Integer point;

    private String message;

    private LocalDateTime orderDateTime;

    public CreateOrderResponse(Long id, Long optionId, Integer quantity, Integer point, String message, LocalDateTime orderDateTime) {
        this.id = id;
        this.optionId = optionId;
        this.quantity = quantity;
        this.point = point;
        this.message = message;
        this.orderDateTime = orderDateTime;
    }

    public static CreateOrderResponse from(Order order, Point point) {
        return new CreateOrderResponse(order.getId(), order.getProductOptionId(), order.getQuantity(), point.getValue(), order.getMessage(), order.getOrderDateTime());
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

    public Integer getPoint() {
        return point;
    }

    public String getMessage() {
        return message;
    }

    public LocalDateTime getOrderDateTime() {
        return orderDateTime;
    }
}