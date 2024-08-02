package gift.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "orders")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long optionId;
    private int quantity;
    private LocalDateTime orderTime;
    private String message;
    private String email;

    public Order() {}

    private Order(Builder builder) {
        this.id = builder.id;
        this.optionId = builder.optionId;
        this.quantity = builder.quantity;
        this.orderTime = builder.orderTime;
        this.message = builder.message;
        this.email = builder.email;
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

    public String getEmail() {
        return email;
    }

    public static class Builder {
        private Long id;
        private Long optionId;
        private int quantity;
        private LocalDateTime orderTime;
        private String message;
        private String email;

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
        public Builder email(String email) {
            this.email = email;
            return this;
        }
        public Order build() {
            return new Order(this);
        }
    }
}