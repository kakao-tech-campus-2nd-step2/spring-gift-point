package gift.dto;

import java.time.LocalDateTime;

public class OrderResponse {
    private final Long id;
    private final Long optionId;
    private final int quantity;
    private final LocalDateTime orderTime;
    private final String message;

    private OrderResponse(Builder builder) {
        this.id = builder.id;
        this.optionId = builder.optionId;
        this.quantity = builder.quantity;
        this.orderTime = builder.orderTime;
        this.message = builder.message;
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

    public LocalDateTime getOrderTime() {
        return orderTime;
    }

    public String getMessage() {
        return message;
    }

    public static class Builder {
        private Long id;
        private Long optionId;
        private int quantity;
        private LocalDateTime orderTime;
        private String message;

        public Builder id(Long id) {
            this.id = id;
            return this;
        }

        public Builder optionId(Long optionId) {
            this.optionId = optionId;
            return this;
        }

        public Builder quantity(int quantity) {
            this.quantity = quantity;
            return this;
        }

        public Builder orderTime(LocalDateTime orderTime) {
            this.orderTime = orderTime;
            return this;
        }

        public Builder message(String message) {
            this.message = message;
            return this;
        }

        public OrderResponse build() {
            return new OrderResponse(this);
        }
    }
}
