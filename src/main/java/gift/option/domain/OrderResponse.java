package gift.option.domain;

import java.time.LocalDateTime;

public class OrderResponse {
    Long id;
    Long optionId;
    Long quantity;
    LocalDateTime orderDateTime = LocalDateTime.now();
    String message;

    // 활용 메서드들
    @Override
    public String toString() {
        return "주문 내역 확인 부탁드립니다. " + '\n' +
                "orderId=" + id +
                ", optionId=" + optionId +
                ", 수량 =" + quantity +
                ", 주문 시간 =" + orderDateTime +
                ", 메시지 ='" + message;
    }

    // Constructors
    public OrderResponse(Long id, Long optionId, Long quantity, LocalDateTime orderDateTime, String message) {
        this.id = id;
        this.optionId = optionId;
        this.quantity = quantity;
        this.orderDateTime = orderDateTime;
        this.message = message;
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
}
