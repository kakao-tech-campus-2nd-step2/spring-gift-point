package gift.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

import java.time.LocalDateTime;

@Entity
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long optionId;
    private int quantity;
    private LocalDateTime orderTime;
    private String message;

    public Order() {}

    private Order(Builder builder) {
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
        public Order build() {
            return new Order(this);
        }
    }
}
