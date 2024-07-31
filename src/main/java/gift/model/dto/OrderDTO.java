package gift.model.dto;

import gift.model.entity.Order;
import gift.model.form.OrderForm;
import java.time.LocalDateTime;

public class OrderDTO {

    private final Long id;
    private final Long optionId;
    private final Long quantity;
    private final String message;
    private final LocalDateTime orderDateTime;

    public OrderDTO(Long id, Long optionId, Long quantity,
        String message, LocalDateTime orderDateTime) {
        this.id = id;
        this.optionId = optionId;
        this.quantity = quantity;
        this.message = message;
        this.orderDateTime = orderDateTime;
    }

    public OrderDTO(Long optionId, Long quantity, String message,
        LocalDateTime orderDateTime) {
        this(null, optionId, quantity, message, orderDateTime);
    }

    public OrderDTO(Order order) {
        this(order.getId(), order.getOptionId(),
            order.getQuantity(), order.getMessage(), order.getOrderDateTime());
    }

    public OrderDTO(OrderForm orderForm, Long userId) {
        this(userId, orderForm.getOptionId(), orderForm.getQuantity(),
            orderForm.getMessage(), LocalDateTime.now());
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

}
