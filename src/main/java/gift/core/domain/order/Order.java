package gift.core.domain.order;

import java.time.LocalDateTime;

public class Order {
    private final Long id;
    private final Long userId;
    private final Long optionId;
    private final Integer quantity;
    private final String message;
    private final LocalDateTime orderedAt;

    public static Order of(Long id, Long userId, Long optionId, Integer quantity, String message, LocalDateTime orderedAt) {
        return new Order(id, userId, optionId, quantity, message, orderedAt);
    }

    public static Order newOrder(Long userId, Long optionId, Integer quantity, String message) {
        return new Order(null, userId, optionId, quantity, message, LocalDateTime.now());
    }

    private Order(Long id, Long userId, Long optionId, Integer quantity, String message, LocalDateTime orderedAt) {
        this.id = id;
        this.userId = userId;
        this.optionId = optionId;
        this.quantity = quantity;
        this.message = message;
        this.orderedAt = orderedAt;
    }

    public Long id() {
        return id;
    }

    public Long userId() {
        return userId;
    }

    public Long optionId() {
        return optionId;
    }

    public Integer quantity() {
        return quantity;
    }

    public String message() {
        return message;
    }

    public LocalDateTime orderedAt() {
        return orderedAt;
    }
}
