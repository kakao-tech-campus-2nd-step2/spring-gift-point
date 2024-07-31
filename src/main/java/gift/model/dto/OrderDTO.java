package gift.model.dto;

import gift.model.entity.Order;
import gift.model.form.OrderForm;
import java.time.LocalDateTime;

public class OrderDTO {

    private final Long id;
    private final Long targetId;
    private final Long itemId;
    private final Long optionId;
    private final Long quantity;
    private final String message;
    private final LocalDateTime orderDateTime;

    public OrderDTO(Long id, Long targetId, Long itemId, Long optionId, Long quantity,
        String message, LocalDateTime orderDateTime) {
        this.id = id;
        this.targetId = targetId;
        this.itemId = itemId;
        this.optionId = optionId;
        this.quantity = quantity;
        this.message = message;
        this.orderDateTime = orderDateTime;
    }

    public OrderDTO(Long targetId, Long itemId, Long optionId, Long quantity, String message,
        LocalDateTime orderDateTime) {
        this(null, targetId, itemId, optionId, quantity, message, orderDateTime);
    }

    public OrderDTO(Order order) {
        this(order.getId(), order.getTargetId(), order.getItemId(), order.getOptionId(),
            order.getQuantity(), order.getMessage(), order.getOrderDateTime());
    }

    public OrderDTO(OrderForm form) {
        this(null, form.getTargetId(), form.getItemId(), form.getOptionId(), form.getQuantity(),
            form.getMessage(), LocalDateTime.now());
    }

    public Long getId() {
        return id;
    }

    public Long getTargetId() {
        return targetId;
    }

    public Long getItemId() {
        return itemId;
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
