package gift.entity;

import gift.domain.Order;
import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "orders")
public class OrderEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "option_id")
    private OptionEntity optionEntity;

    @Column(name = "quantity", nullable = false)
    private Long quantity;

    @Column(name = "message", nullable = false)
    private String message;

    @Column(name = "date", nullable = false)
    private LocalDateTime orderDateTime;

    public OrderEntity(OptionEntity optionEntity, Long quantity, String message, LocalDateTime orderDateTime) {
        this.optionEntity = optionEntity;
        this.quantity = quantity;
        this.message = message;
        this.orderDateTime = orderDateTime;
    }

    public OrderEntity(Long id, OptionEntity optionEntity, Long quantity, String message,
        LocalDateTime orderDateTime) {
        this.id = id;
        this.optionEntity = optionEntity;
        this.quantity = quantity;
        this.message = message;
        this.orderDateTime = orderDateTime;
    }

    public Long getId() {
        return id;
    }

    public OptionEntity getOptionEntity() {
        return optionEntity;
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

    public static Order toDto(OrderEntity orderEntity) {
        return new Order(
            orderEntity.getId(),
            orderEntity.getOptionEntity().getId(),
            orderEntity.getQuantity(),
            orderEntity.getMessage(),
            orderEntity.getOrderDateTime()
        );
    }
}