package gift.dto;

import gift.entity.Option;
import gift.entity.Order;
import gift.entity.Product;
import java.time.LocalDateTime;

public class OrderFindAllResponse {

    private Long orderId;
    private Long productId;
    private Long optionId;
    private int quantity;
    private LocalDateTime orderDateTime;
    private String message;

    public OrderFindAllResponse(Order order, Product product, Option option) {
        this(order.getId(), product.getId(), option.getId(),
            order.getQuantity(), order.getCreatedDate(), order.getMessage());
    }

    public OrderFindAllResponse(Long orderId, Long productId, Long optionId, int quantity,
        LocalDateTime orderDateTime, String message) {
        this.orderId = orderId;
        this.productId = productId;
        this.optionId = optionId;
        this.quantity = quantity;
        this.orderDateTime = orderDateTime;
        this.message = message;
    }

    public Long getOrderId() {
        return orderId;
    }

    public Long getProductId() {
        return productId;
    }

    public Long getOptionId() {
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
