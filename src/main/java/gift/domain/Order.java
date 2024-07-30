package gift.domain;

import gift.dto.OrderDto;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDateTime;

@Entity
@Table(name = "orders")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long optionId;

    @Column(nullable = false)
    private int quantity;

    @Column(nullable = false)
    private LocalDateTime orderDateTime;

    @Column(nullable = false)
    private String message;

    public Order() {
    }

    public Order(Long optionId, int quantity, String message) {
        this.optionId = optionId;
        this.quantity = quantity;
        this.message = message;
        this.orderDateTime = LocalDateTime.now();
    }

    public OrderDto toOrderDto() {
        return new OrderDto(this.id, this.optionId, this.quantity, this.orderDateTime,
            this.message);
    }
}
