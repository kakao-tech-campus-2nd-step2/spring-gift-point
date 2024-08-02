package gift.order.domain;

import jakarta.persistence.*;
import org.springframework.cglib.core.Local;

import java.time.LocalDateTime;

@Entity
@Table(name = "orders")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    Long optionId;
    Long quantity;
    LocalDateTime orderDateTime;
    String message;
    Long pointsUsed;
    Long pointsReceived;
    Long payment;

    // Constructors
    public Order(Long optionId, Long quantity, LocalDateTime orderDateTime, String requestMessage, Long pointsUsed, Long pointsReceived, Long payment) {
        this.optionId = optionId;
        this.quantity = quantity;
        this.orderDateTime = orderDateTime;
        this.message = requestMessage;
        this.pointsUsed = pointsUsed;
        this.pointsReceived = pointsReceived;
        this.payment = payment;
    }
    public Order(Long optionId, Long quantity, LocalDateTime orderDateTime, String requestMessage) {
        this.optionId = optionId;
        this.quantity = quantity;
        this.orderDateTime = orderDateTime;
        this.message = requestMessage;
    }

    public Order() {

    }

    // getter, setter

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public Long getPointsUsed() {
        return pointsUsed;
    }

    public void setPointsUsed(Long pointsUsed) {
        this.pointsUsed = pointsUsed;
    }

    public Long getPointsReceived() {
        return pointsReceived;
    }

    public void setPointsReceived(Long pointsReceived) {
        this.pointsReceived = pointsReceived;
    }

    public Long getPayment() {
        return payment;
    }

    public void setPayment(Long payment) {
        this.payment = payment;
    }
}
