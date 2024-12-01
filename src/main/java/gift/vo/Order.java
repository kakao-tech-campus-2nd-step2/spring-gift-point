package gift.vo;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

@Entity
@Table(name = "orders")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    Long memberId;

    @NotNull
    Long optionId;

    @NotNull
    private int quantity;

    @NotNull
    private LocalDateTime orderDateTime;

    private String message;

    public Order() {
    }

    public Order(Long memberId, Long optionId, int quantity, LocalDateTime orderDateTime, String message) {
        this(null, memberId, optionId, quantity, orderDateTime, message);
    }

    public Long getId() {
        return id;
    }

    public @NotNull Long getMemberId() {
        return memberId;
    }

    @NotNull
    public int getQuantity() {
        return quantity;
    }

    public @NotNull Long getOptionId() {
        return optionId;
    }

    public @NotNull LocalDateTime getOrderDateTime() {
        return orderDateTime;
    }

    public String getMessage() {
        return message;
    }

    public Order(Long id, Long memberId, Long optionId, int quantity, LocalDateTime orderDateTime, String message) {
        this.id = id;
        this.memberId = memberId;
        this.optionId = optionId;
        this.quantity = quantity;
        this.orderDateTime = orderDateTime;
        this.message = message;
    }

}
