package gift.order;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "ORDERS")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    @Column
    Long optionId;
    @Column
    Long quantity;
    @Column
    LocalDateTime orderDateTime;
    @Column
    String message;
    @Column
    Long userId;

    public Order() {
    }

    public Order(Long optionId, Long quantity, LocalDateTime orderDateTime, String message, Long userId) {
        this.optionId = optionId;
        this.quantity = quantity;
        this.orderDateTime = orderDateTime;
        this.message = message;
        this.userId = userId;
    }

    public Order(OrderRequest orderRequest, LocalDateTime orderDateTime, Long userId){
        this.optionId = orderRequest.getOptionId();
        this.quantity = orderRequest.getQuantity();
        this.orderDateTime = orderDateTime;
        this.message = orderRequest.getMessage();
        this.userId = userId;
    }

    public Long getId() {
        return id;
    }

    public Long getOptionId() {
        return optionId;
    }

    public void setOptionId(Long optionId) {
        this.optionId = optionId;
    }

    public Long getQuantity() {
        return quantity;
    }

    public void setQuantity(Long quantity) {
        this.quantity = quantity;
    }

    public LocalDateTime getOrderDateTime() {
        return orderDateTime;
    }

    public void setOrderDateTime(LocalDateTime orderDateTime) {
        this.orderDateTime = orderDateTime;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }
}
