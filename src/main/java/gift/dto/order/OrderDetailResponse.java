package gift.dto.order;

import gift.domain.Order;

public class OrderDetailResponse {
    private final Long id;

    private final Long optionId;

    private final int quantity;

    private final String message;

    private final int usedPoint;

    private final int receivedPoint;

    private final int totalPrice;

    public OrderDetailResponse(Order order) {
        this.id = order.getId();
        this.optionId = order.getOption().getId();
        this.quantity = order.getQuantity();
        this.message = order.getMessage();
        this.usedPoint = order.getUsedPoint();
        this.receivedPoint = order.getReceivedPoint();
        this.totalPrice = order.getTotalPrice();
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

    public int getUsedPoint() {
        return usedPoint;
    }

    public int getReceivedPoint() {
        return receivedPoint;
    }

    public int getTotalPrice() {
        return totalPrice;
    }
}
