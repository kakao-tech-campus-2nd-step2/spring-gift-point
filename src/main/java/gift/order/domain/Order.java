package gift.order.domain;

import gift.payment.domain.PaymentRequest;
import gift.payment.domain.PaymentResponse;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "orders")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long userId;
    private Long productId;
    private Long optionId;
    private Long quantity;
    private String message;
    private String phoneNumber;

    private Boolean isReceipt;


    private final LocalDateTime createdAt = LocalDateTime.now();

    public Order(Long userId, PaymentRequest request) {
        this.userId = userId;
        this.productId = request.getProductId();
        this.optionId = request.getOptionId();
        this.quantity = request.getQuantity();
        this.message = request.getMessage();
        this.phoneNumber = request.getPhone();
        this.isReceipt = request.isReceipt();
    }

    public Order() {

    }

    public PaymentResponse toOrderCreateResponse() {
        return new PaymentResponse(
                id,
                optionId,
                quantity,
                createdAt.toString(),
                message,
                true
        );
    }

    public Long getId() {
        return id;
    }

    public Long getUserId() {
        return userId;
    }

    public Long getProductId() {
        return productId;
    }

    public Long getOptionId() {
        return optionId;
    }

    public Long getQuantity() {
        return quantity;
    }

    public String getMessage() {
        return message;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public Boolean getReceipt() {
        return isReceipt;
    }

    public LocalDateTime getOrderDateTime() {
        return createdAt;
    }
}
