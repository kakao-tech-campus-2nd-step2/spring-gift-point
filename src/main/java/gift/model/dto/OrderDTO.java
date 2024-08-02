package gift.model.dto;

import gift.model.entity.Order;
import gift.model.form.OrderForm;
import java.time.LocalDateTime;

public class OrderDTO {

    private final Long id;
    private final Long productId;
    private final Long optionId;
    private final Long quantity;
    private final String message;
    private final LocalDateTime orderDateTime;
    private final Long totalPrice;

    public OrderDTO(Long id, Long productId, Long optionId, Long quantity,
        String message, LocalDateTime orderDateTime, Long totalPrice) {
        this.id = id;
        this.productId = productId;
        this.optionId = optionId;
        this.quantity = quantity;
        this.message = message;
        this.orderDateTime = orderDateTime;
        this.totalPrice = totalPrice;
    }

    public OrderDTO(Long productId, Long optionId, Long quantity, String message,
        LocalDateTime orderDateTime, Long totalPrice) {
        this(null, productId, optionId, quantity, message, orderDateTime, totalPrice);
    }

    public OrderDTO(Order order) {
        this(order.getId(), order.getProductId(), order.getOptionId(),
            order.getQuantity(), order.getMessage(), order.getOrderDateTime(),
            order.getTotalPrice());
    }

    public OrderDTO(OrderForm orderForm, Long userId, Long totalPrice) {
        this(userId, orderForm.getOptionId(), orderForm.getQuantity(),
            orderForm.getMessage(), LocalDateTime.now(), totalPrice);
    }

    public Long getId() {
        return id;
    }

    public Long getOptionId() {
        return optionId;
    }

    public Long getQuantity() {
        return quantity;
    }

    public String getMessage() {
        return message;
    }

    public LocalDateTime getOrderDateTime() {
        return orderDateTime;
    }

    public Long getProductId() {
        return productId;
    }

    public Long getTotalPrice() {
        return totalPrice;
    }
}
