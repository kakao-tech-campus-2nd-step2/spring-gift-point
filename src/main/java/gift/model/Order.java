package gift.model;

import gift.dto.OrderResponse;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

@Entity(name = "orders")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "option_id", nullable = false)
    private Option option;
    @NotNull
    private Long quantity;
    @NotNull
    private LocalDateTime orderDateTime;
    @NotNull
    @NotBlank
    private String message;

    public Order() {
        this.orderDateTime = LocalDateTime.now();
    }

    public Order(Option option, Long quantity, String message) {
        this.option = option;
        this.quantity = quantity;
        this.message = message;
        this.orderDateTime = LocalDateTime.now();
    }

    public Order(Long id, Option option, Long quantity, String message, LocalDateTime orderDateTime) {
        this.id = id;
        this.option = option;
        this.quantity = quantity;
        this.message = message;
        this.orderDateTime = orderDateTime;
    }

    public Long getId() {
        return id;
    }

    public Option getOption() {
        return option;
    }

    public Long getQuantity() {
        return quantity;
    }

    public LocalDateTime getOrderDateTime() {
        return orderDateTime;
    }

    public String getMessage() {
        return message;
    }

    public OrderResponse toDto() {
        return new OrderResponse(
                this.id,
                this.option.getId(),
                this.quantity,
                this.orderDateTime,
                this.message
        );
    }
}
