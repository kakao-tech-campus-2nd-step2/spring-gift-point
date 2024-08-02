package gift.order.dto;

import java.time.LocalDateTime;

public class OrderResponse {
    Long id;
    Long optionId;
    Long quantity;
    LocalDateTime orderDateTime;
    Long pointsUsed;
    Long pointsReceived;
    Long payment;
    String message;

    // 활용 메서드들
    @Override
    public String toString() {
        return "주문 내역 확인 부탁드립니다. " + '\n' +
                "orderId=" + id +
                ", optionId=" + optionId +
                ", 수량 =" + quantity +
                ", 주문 시간 =" + orderDateTime +
                ", 메시지 ='" + message +
                ", 사용한 포인트 =" + pointsUsed +
                ", 받은 포인트 =" + pointsReceived +
                ", 결제 금액 =" + payment;
    }

    // Constructors
    public OrderResponse(
            Long id,
            Long optionId,
            Long quantity,
            LocalDateTime orderDateTime,
            String message,
            Long pointsUsed,
            Long pointsReceived,
            Long payment
    ) {
        this.id = id;
        this.optionId = optionId;
        this.quantity = quantity;
        this.orderDateTime = orderDateTime;
        this.message = message;
        this.pointsUsed = pointsUsed;
        this.pointsReceived = pointsReceived;
        this.payment = payment;
    }

    // Getters and setters
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
