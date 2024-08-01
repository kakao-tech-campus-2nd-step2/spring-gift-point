package gift.web.dto.response.order;

import gift.domain.Order;

public class OrderResponse {

    private Long productId;

    private Long optionId;

    private Integer quantity;

    private String message;

    public OrderResponse(Long productId, Long optionId, Integer quantity, String message) {
        this.productId = productId;
        this.optionId = optionId;
        this.quantity = quantity;
        this.message = message;
    }

    public static OrderResponse from(Order order) {
        return new OrderResponse(order.getProductId(), order.getProductOptionId(), order.getQuantity(), order.getMessage());
    }

    public Long getProductId() {
        return productId;
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
}