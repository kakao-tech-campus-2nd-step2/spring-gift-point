package gift.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.time.LocalDateTime;

@Entity
@Table(name = "orders")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "option_id", nullable = false)
    private Option option;

    @Column(nullable = false)
    private int quantity;

    @Column(nullable = false)
    private LocalDateTime orderDateTime;

    @Column(length = 500)
    private String message;

    protected Order() {
    }

    private Order(OrderBuilder builder) {
        this.id = builder.id;
        this.option = builder.option;
        this.quantity = builder.quantity;
        this.orderDateTime = builder.orderDateTime;
        this.message = builder.message;
    }

    public Long getId() {
        return id;
    }

    public Option getOption() {
        return option;
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

    public static class OrderBuilder {
        private Long id;
        private Option option;
        private int quantity;
        private LocalDateTime orderDateTime;
        private String message;

        public OrderBuilder id(Long id) {
            this.id = id;
            return this;
        }

        public OrderBuilder option(Option option) {
            this.option = option;
            return this;
        }

        public OrderBuilder quantity(int quantity) {
            this.quantity = quantity;
            return this;
        }

        public OrderBuilder orderDateTime(LocalDateTime orderDateTime) {
            this.orderDateTime = orderDateTime;
            return this;
        }

        public OrderBuilder message(String message) {
            this.message = message;
            return this;
        }

        public Order build() {
            return new Order(this);
        }
    }
}
