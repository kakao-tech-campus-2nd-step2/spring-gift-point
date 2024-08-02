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
    private Long optionId;
    private Long quantity;
    private String message;
    private String phoneNumber;

    private Boolean isReceipt;

    private final LocalDateTime orderDateTime = LocalDateTime.now();

    public Order(Long userId, PaymentRequest request) {
        this.userId = userId;
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
                orderDateTime.toString(),
                message,
                true
        );
    }
}
