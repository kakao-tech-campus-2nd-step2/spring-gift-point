package gift.order.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long userId;
    private Long optionId;
    private Long quantity;
    private String message;

    private final LocalDateTime orderDateTime = LocalDateTime.now();

    public Order(Long userId, CreateOrderRequest request) {
        this.userId = userId;
        this.optionId = request.getOptionId();
        this.quantity = request.getQuantity();
        this.message = request.getMessage();
    }

    public Order() {

    }

    public OrderCreateResponse toOrderCreateResponse() {
        return new OrderCreateResponse(id, optionId, quantity, orderDateTime, message);
    }
}
