package gift.order;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Entity
public class OrderInfo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private Long optionId;

    @Column(name = "quantity")
    private Integer quantity;

    @Column(name = "order_date_time")
    private LocalDateTime orderDateTime;

    @Column(name = "message")
    private String message;

    public void setMemberId(Long memberId) {
        this.memberId = memberId;
    }

    private Long memberId;

    protected OrderInfo(){}

    public OrderInfo(Long optionId, Integer quantity, LocalDateTime orderDateTime, String message) {
        this.optionId = optionId;
        this.quantity = quantity;
        this.orderDateTime = orderDateTime;
        this.message = message;
    }

    public Long getId() {
        return id;
    }

    public Long getOptionId() {
        return optionId;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public LocalDateTime getOrderDateTime() {
        return orderDateTime;
    }

    public String getMessage() {
        return message;
    }

    public Long getMemberId() {
        return memberId;
    }
}
